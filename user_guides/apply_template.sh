#!/bin/sh
#
# apply_template
#
export TEMPLATE=$PWD/Template/src/main/jbake/content
SPECS=`ls -d [a-z]*/. | grep -v javaee`

for i in $( (cd $TEMPLATE; ls *.adoc) )
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		cp  $TEMPLATE/$i $j/src/main/jbake/content/$i
	done
done

for i in $(cd $TEMPLATE; ls *.inc)
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		[ -f $j/src/main/jbake/content/$i ] || \
		cp  $TEMPLATE/$i $j/src/main/jbake/content/$i
	done
done

for i in README
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		cp  $TEMPLATE/$i $j/src/main/jbake/content/$i
	done
done

echo "=== attributes.conf ==="
for j in $SPECS
do
	(
	echo $j:
	cd $j/src/main/jbake/content
	(
	echo "ed - $TEMPLATE/attributes.conf <<'EOF'"
	grep '^:' attributes.conf | \
	  sed -e 's;:\([a-zA-Z]*\):*\(.*\);g/:\1:/s,.*,:\1:\2,;'
	echo 'g/:JavaTestVersion:/s/.*/:JavaTestVersion: 5.0/'
	echo w attributes.conf
	echo q
	echo EOF
	) > /tmp/ed1
	mv attributes.conf attributes.conf-
	sh /tmp/ed1
	)
done
