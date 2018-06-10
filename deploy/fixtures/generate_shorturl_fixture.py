from mimesis.schema import Field, Schema
from mimesis.enums import Gender
_ = Field('en')
description = (
 lambda: {
     '_class': 'account',
     'name': _('text.word'),
     'email': _('person.email'),
     'taxnumber': _('person.identifier',mask="###-###-###-##"),
     'maxShortcuts': _('numbers.between', minimum=10, maximum=100),
     'currentShortcuts': _('numbers.between', minimum=0, maximum=10),
     'shortcuts': [
        {
        'shortcut': _('cryptographic.token', entropy=16),
        'url': 'http://'+_('text.word')+_('internet.top_level_domain')+'/'+_('text.word'),
        'expiryPolicy' : {
            '_class' : 'redirect',
            'max' : _('numbers.between',minimum=1000,maximum=1000000)
        }
        }
     ]
 }
)

schema = Schema(schema=description)
print (schema.create(iterations=100))
