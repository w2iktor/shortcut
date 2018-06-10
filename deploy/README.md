import to mongo from JSON:

  ansible mongod -i mongod.inventory -u root -a "mongoimport --drop -c posts --file /mnt/backup/posts-to-import.json"
