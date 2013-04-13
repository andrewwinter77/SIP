#!/bin/sh

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=jsr289:remove"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/extension=org.andrewwinter.jsr289.jboss:remove"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command=":reload"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module remove --name=javax.servlet.sip.api"
cp module.javax.servlet.sip.api.xml /tmp
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module add --name=javax.servlet.sip.api --resources=/Users/andrew/Documents/reference/sip/SipServlet-v1.1-Spec/lib/sipservlet-1_1-api.jar --module-xml=/tmp/module.javax.servlet.sip.api.xml"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module add --name=org.postgresql --resources=/Users/andrew/Dev/postgresql-9.1-902.jdbc4.jar --dependencies=javax.api,javax.transaction.api,javax.servlet.api"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=datasources/jdbc-driver=postgresql-driver:add(driver-name=postgresql-driver, driver-class-name=org.postgresql.Driver, driver-module-name=org.postgresql)"


${JBOSS_HOME}/bin/jboss-cli.sh --connect command="xa-data-source add --name=sipappDS --driver-name=postgresql-driver --jndi-name=java:/datasources/sipappDS --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLValidConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.postgres.PostgreSQLExceptionSorter  --xa-datasource-class=org.postgresql.xa.PGXADataSource --min-pool-size=5 --max-pool-size=100 --pool-prefill=true --pad-xid=true"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=datasources/xa-data-source=sipappDS/xa-datasource-properties=ServerName:add(value=localhost)"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=datasources/xa-data-source=sipappDS/xa-datasource-properties=DatabaseName:add(value=sipapp)"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=datasources/xa-data-source=sipappDS/xa-datasource-properties=User:add(value=sipapp)"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=datasources/xa-data-source=sipappDS/xa-datasource-properties=PortNumber:add(value=5432)"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=datasources/xa-data-source=sipappDS/xa-datasource-properties=Password:add(value=sipapp123)"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="xa-data-source --name=sipappDS enable"



${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module remove --name=org.andrewwinter.jsr289.jboss"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module add --name=org.andrewwinter.jsr289.jboss --resources=/Users/andrew/.m2/repository/org/andrewwinter/sip/JBossEap61Module/1.0-SNAPSHOT/JBossEap61Module-1.0-SNAPSHOT-jar-with-dependencies.jar --dependencies=
javax.api,org.jboss.staxmapper,org.jboss.as.ee,org.jboss.as.controller,org.jboss.as.naming,org.jboss.as.server,org.jboss.modules,org.jboss.msc,org.jboss.logging,org.jboss.vfs,javax.xml.bind.api,org.jboss.jandex,javax.servlet.sip.api,javax.annotation.api,org.jboss.netty,org.jboss.metadata,org.jboss.as.web,org.jboss.as.weld,javax.enterprise.api"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/extension=org.andrewwinter.jsr289.jboss:add"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=jsr289:add"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command=":reload"
