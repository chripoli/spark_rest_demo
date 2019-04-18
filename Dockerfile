FROM ubuntu:16.04

ARG BASE_DIR=/apps/spark_demo

RUN mkdir -p ${BASE_DIR}

RUN apt-get update && apt-get install -y openjdk-8-jre wget

WORKDIR ${BASE_DIR}
RUN wget http://apache.lauf-forum.at/spark/spark-2.4.1/spark-2.4.1-bin-hadoop2.7.tgz
RUN tar xzvf spark-2.4.1-bin-hadoop2.7.tgz
RUN rm spark-2.4.1-bin-hadoop2.7.tgz
RUN ln -s spark-2.4.1-bin-hadoop2.7 spark
RUN cp ${BASE_DIR}/spark/conf/spark-env.sh.template ${BASE_DIR}/spark/conf/spark-env.sh
RUN echo "SPARK_MASTER_HOST=master" >>  ${BASE_DIR}/spark/conf/spark-env.sh

ENV SPARK_HOME ${BASE_DIR}/spark
ENV SPARK_NO_DAEMONIZE true

ENTRYPOINT [ "/apps/spark_demo/spark/sbin/start-master.sh"]