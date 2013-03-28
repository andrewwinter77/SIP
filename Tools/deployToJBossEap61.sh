#!/bin/sh

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=jsr289:remove"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/extension=org.andrewwinter.jsr289.jboss:remove"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command=":reload"

cp module.javax.servlet.sip.api.xml /tmp
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module add --name=javax.servlet.sip.api --resources=/Users/andrew/Documents/reference/sip/SipServlet-v1.1-Spec/lib/sipservlet-1_1-api.jar --module-xml=/tmp/module.javax.servlet.sip.api.xml"
#${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module add --name=javax.servlet.sip.api --resources=/Users/andrew/Documents/reference/sip/SipServlet-v1.1-Spec/lib/sipservlet-1_1-api.jar --dependencies=javax.servlet.api"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module remove --name=org.andrewwinter.jsr289.jboss"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="module add --name=org.andrewwinter.jsr289.jboss --resources=/Users/andrew/.m2/repository/org/andrewwinter/sip/JBossEap61Module/1.0-SNAPSHOT/JBossEap61Module-1.0-SNAPSHOT-jar-with-dependencies.jar --dependencies=
javax.api,org.jboss.staxmapper,org.jboss.as.ee,org.jboss.as.controller,org.jboss.as.naming,org.jboss.as.server,org.jboss.modules,org.jboss.msc,org.jboss.logging,org.jboss.vfs,javax.xml.bind.api,org.jboss.jandex,javax.servlet.sip.api,javax.annotation.api,org.jboss.netty,org.jboss.metadata"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/extension=org.andrewwinter.jsr289.jboss:add"
${JBOSS_HOME}/bin/jboss-cli.sh --connect command="/subsystem=jsr289:add"

${JBOSS_HOME}/bin/jboss-cli.sh --connect command=":reload"
