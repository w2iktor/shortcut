from random import randint, choice
import json

from mimesis.schema import Field, Schema

from mimesis.providers.base import BaseProvider


_ = Field('en')

# schema of redirect shortcode
redirect_shortcode = (
    lambda: {
        'shortcut': _('cryptographic.token', entropy=16),
        'url': 'http://' + _('text.word') + _('internet.top_level_domain') + '/' + _('text.word'),
        'expiryPolicy': {
            '_class': 'redirect',
            'max': _('numbers.between', minimum=1000, maximum=1000000)
        }
    }
)

redirect_shortcode_schema = Schema(schema=redirect_shortcode)

# schema of datetime shortcode
datetime_shortcode = (
    lambda: {
        'shortcut': _('cryptographic.token', entropy=16),
        'url': 'http://' + _('text.word') + _('internet.top_level_domain') + '/' + _('text.word'),
        'expiryPolicy': {
            '_class': 'datetime',
            'validUntil': {
               '$date': _('datetime.timestamp', posix=True, start=2000) * 1000
            }
        }
    }
)

datetime_shortcode_schema = Schema(schema=datetime_shortcode)

# provider of list of shortcodes
class Shorturl(BaseProvider):

    @staticmethod
    def shortcodes():
        schemas = (redirect_shortcode_schema, datetime_shortcode_schema)
        number_of_shortcodes = randint(2, 10)
        shortcodes = []
        for i in range(number_of_shortcodes):
            shortcodes.append( \
            choice(schemas).create(iterations=1)[0])
        return shortcodes


_ = Field('en', providers=[Shorturl])

#schema of account object
description = (
    lambda: {
        '_class': 'account',
        'name': _('text.word'),
        'email': _('person.email'),
        'taxnumber': _('person.identifier', mask="###-###-###-##"),
        'maxShortcuts': _('numbers.between', minimum=10, maximum=100),
        'currentShortcuts': _('numbers.between', minimum=0, maximum=10),
        'shortcuts': _('shorturl.shortcodes')
    }
)

schema = Schema(schema=description)
shortcuts = []
for i in range(10000):
    account = schema.create(iterations=1)[0]
    shortcuts += [shortcuts['shortcut'] for shortcuts in account['shortcuts']]
    # so Wiktor nazi is feed with proper JSONs
    print(json.dumps(account))

# write generated shortucts, for use in clicks fixture
with open("shortcuts.lst", "w") as file:
    file.write("\n".join(shortcuts))
