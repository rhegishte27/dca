#TOMCAT_LOCALHOSTPORT=$1
#WEBAPP_NAME=$2
API_URL="https://localhost/api/v1"
REACT_ENV_PATH="./react/.env"
#mkdir -p $REACT_ENV_PATH
sudo cat <<EOF > $REACT_ENV_PATH
API_URL=$API_URL
EOF