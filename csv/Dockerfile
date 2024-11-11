FROM python:3.7-slim

RUN apt-get update && \
    apt-get install -y default-jdk gcc g++ wget && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64
ENV PATH "$JAVA_HOME/bin:$PATH"

RUN pip install jaydebeapi

RUN wget -O /h2.jar https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar

COPY problem.csv .
COPY insert_csv.py .

CMD ["python", "insert_csv.py"]
