webServerHost=${webServerHost}
webServerPort=${webServerPort}
ts_home=${project.build.directory}/jakartaee

bin.dir=${bin.dir}
jimage.dir=${jimage.dir}
sigTestClasspath=${sigTestClasspath}${path.separator}${jimage.dir}/java.base${path.separator}${jimage.dir}/java.rmi${path.separator}${jimage.dir}/java.sql${path.separator}${jimage.dir}/java.naming