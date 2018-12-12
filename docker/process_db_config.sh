#!/bin/bash -x

if [ $# -lt 2 ];then
  echo "Did not receive the required arguments for running this script."
  echo "Please specify the Database type and TCK home directory."
  echo "eg: ./process_db_config.sh Oracle "
  exit 1
fi

DB_TYPE=$2
TS_HOME=$3
DB_CFG_FILE="conf/${DB_TYPE}.cfg"

if [ ! -f "${DB_CFG_FILE}" ];then
  echo "The specified Database type $DB_TYPE is not yet supported for running the TCK."
  exit 1
fi

while IFS="" read -r keyvalue_pair || [ -n "$keyvalue_pair" ]
do
  if [[ \#* == $keyvalue_pair || -z $keyvalue_pair ]]; then
    echo "Ignoring comment"
  else
    printf '%s\n' "$keyvalue_pair"
    key=`echo $keyvalue_pair | cut -f1 -d=`
    value=`echo $keyvalue_pair | cut -f2 -d=`
    echo "Key: $key"
    echo "Value: $value"
    if [[ ! -z "$key" && ! -z "$value" ]]; then
      sed -i "s#$key#$value#g" $TS_HOME/bin/ts.jte
    fi
  fi
done <${DB_CFG_FILE}
