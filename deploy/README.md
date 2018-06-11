# working with ansible

import to mongo from JSON:

  ansible mongod -i mongod.inventory -u root -a "mongoimport --drop -c posts --file /mnt/backup/posts-to-import.json"

copy file to remote server:

  ansible mongod -i mongod.inventory -u root -m copy -a "src=posts-to-import.json dest=/mnt/backup/posts-to-import.json"

and to setup whole training from scratch:

  ansible-playbook -i mongod.inventory playbook.yml -u root

# generating fixtures

this is two part process, first script generates accounts (with shortcodes):

  python fixtures/generate_shorturl_fixture_accounts.py > accounts.json

this creates two files `accounts.json` and `shortcuts.lst` (this one contains
list of generated shortcuts). Once you have it, you can generate clicks.

  python fixtures/generate_shorturl_fixture_clicks.py > clicks.json

you can also write it directly to mongo with:

  python fixtures/generate_shorturl_fixture_accounts.py | mongoimport --drop -c accounts
  python fixtures/generate_shorturl_fixture_clicks.py | mongoimport --drop -c clicks
