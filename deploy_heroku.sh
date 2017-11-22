#!/bin/sh
wget -qO- https://toolbelt.heroku.com/install-ubuntu.sh | sh

heroku plugins:install heroku-container-registry
docker login --username=ripreal --password=$HEROKU_API_KEY registry.heroku.com
heroku container:push web --app $HEROKU_APP_NAME

docker login --username=_ --password=${YOUR_TOKEN} registry.heroku.com
