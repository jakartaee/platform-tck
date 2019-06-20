#!/bin/sh
#
# apply_template
#
export TEMPLATE=$PWD/Template/src/main/jbake
SPECS=${SPECS:-`ls -d [a-z]*/. | grep -v javaee`}

update() {
	cmp "$1" "$2" > /dev/null || cp "$1" "$2"
}

for i in $( (cd $TEMPLATE/content; ls *.adoc) )
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		update  $TEMPLATE/content/$i $j/src/main/jbake/content/$i
	done
done

for i in $( (cd $TEMPLATE/content; ls *.inc) )
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		[ -f $j/src/main/jbake/content/$i ] || \
		cp  $TEMPLATE/content/$i $j/src/main/jbake/content/$i
	done
done

for i in README
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		update  $TEMPLATE/content/$i $j/src/main/jbake/content/$i
	done
done

echo "=== attributes.conf ==="
for j in $SPECS
do
	(
	echo $j:
	cd $j/src/main/jbake/content
	(
	echo "ed - $TEMPLATE/content/attributes.conf <<'EOF'"
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

for i in $(cd $TEMPLATE; ls templates/*.ftl assets/css/*.css assets/img/*.png)
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		update  $TEMPLATE/$i $j/src/main/jbake/$i
	done
done

for i in $(cd $TEMPLATE/../../theme; ls *)
do
	echo "=== $i ==="
	for j in $SPECS
	do
		echo $j:
		[ -d $j/src/theme ] || mkdir $j/src/theme
		update  $TEMPLATE/../../theme/$i $j/src/theme/$i
	done
done
