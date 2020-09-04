The set of instructions provided below is tested with Linux platform, with a sample SpecAssertions.xml

    1. Pull CTS Tools source to a local directory, let this be BASE_DIR
       git clone https://github.com/eclipse-ee4j/jakartaee-tck-tools

    2. Download the CTS bundle from http://download.eclipse.org/ee4j/jakartaee-tck/jakartaee9/nightly/jakartaeetck-9.0.0.zip and extract to a local directory. Let this local directory be TS_HOME

    3. Set the following environment variables
        export JAVA_HOME=<>
        export TS_HOME=<jakartaeetck>
        export ANT_HOME=<ANT_HOME>
        export PATH=$JAVA_HOME/bin:$TS_HOME/bin:$ANT_HOME/bin:$PATH

    4. cd $BASE_DIR/tools/xsl-transformer/scripts
    ln -s run.sh run // Required as the script assert-num-html.sh just calls ./run
    chmod 777 *

    5. cd $BASE_DIR/tools/scripts/
    chmod 777 assert-num-html.sh

    6. Search for "Transform the numbered XML assertion document to HTML" in assert-num-html.sh and insert the following text to sort the IDs if not present.
    #####
    # Sort the IDs
    #####
    XML_ASSERT_NUMS_FILE="sort-assert-id-nums.xml"
    INPUT_FILE=${OUTPUT_FILE}
    OUTPUT_DIR=${2}
    OUTPUT_FILE="${OUTPUT_DIR}/${XML_ASSERT_NUMS_FILE}"
    XSL_FILE="../../../docs/xsl/assertions/sort-assert-id.xsl"
    CMD="./run ${INPUT_FILE} ${XSL_FILE} ${OUTPUT_FILE}"
    echo "\n\n$CMD\n\n"
    $CMD

    7. cd $BASE_DIR/tools
    ant clean compile
    export CLASSPATH=$BASE_DIR/tools/xsl-transformer/classes:$CLASSPATH

    8. Create a local directory to copy the spec assertions for the given technology area and copy all the files (at least you need all the dependent files)
    mkdir /mysource
    cp <local directory>/internal/docs/servlet/*.* /mysource

    9. For servlet, I have noticed ServletSpecAssertions.xml using encoding="US-ASCII" but the xml file containing non-ASCII character set. So remove encoding="US-ASCII" from <?xml version="1.0" encoding="US-ASCII"?>

    10. Create the output directory for the generated files.
    mkdir /mytarget

    11. cd $BASE_DIR/tools/scripts/
    ./assert-num-html.sh /mysource/<SpecAssertions.xml> /mytarget

    12. Verify the contents of /mytarget/assertions.html