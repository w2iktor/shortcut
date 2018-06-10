from mimesis.schema import Field, Schema
from mimesis.enums import Gender
_ = Field('en')
description = (
 lambda: {
     '_class': 'click',
     'shortcut': _('cryptographic.token', entropy=16),
     'localDateTime': _('datetime.datetime').isoformat(),
     'ipAddress': _('person.email'),
     'os': _('person.identifier',mask="###-###-###-##"),
     'agent': _('numbers.between', minimum=10, maximum=100),
     'referer': 'http://'+_('text.word')+_('internet.top_level_domain')+'/'+_('text.word')
 }
)

schema = Schema(schema=description)
print (schema.create(iterations=100))
