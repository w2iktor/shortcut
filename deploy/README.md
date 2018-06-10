import to mongo from JSON:

  ansible mongod -i mongod.inventory -u root -a "mongoimport --drop -c posts --file /mnt/backup/posts-to-import.json"

copy file to remote server:

  ansible mongod -i mongod.inventory -u root -m copy -a "src=posts-to-import.json dest=/mnt/backup/posts-to-import.json"
