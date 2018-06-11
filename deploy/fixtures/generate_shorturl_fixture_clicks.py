from mimesis.schema import Field, Schema
from mimesis.providers.base import BaseProvider

from random import choice


# provider of shortcuts based on existing list
class Shortcuts(BaseProvider):

    @staticmethod
    def choice(shortcuts):
        return choice(shortcuts)

# read all shortcuts
with open("shortcuts.lst", "r") as f:
    shortcuts = [l.strip() for l in f.readlines()]

_ = Field('en', providers=[Shortcuts])

description = (
    lambda: {
        '_class': 'click',
        'shortcut': _('shortcuts.choice', shortcuts=shortcuts),
        'localDateTime': {
            '$date': _('datetime.timestamp', posix=True, start=2000) * 1000
        },
        'ipAddress': _('internet.ip_v4'),
        'os': _('development.os'),
        'agent': _('internet.user_agent'),
        'referer': 'http://' + _('text.word') + _('internet.top_level_domain') + '/' + _('text.word')
    }
)

schema = Schema(schema=description)
for i in range(1000000):
    print(schema.create(iterations=1)[0])
