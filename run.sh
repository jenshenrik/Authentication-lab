#!/usr/bin/bash

java -classpath bin -Djava.security.auth.login.config==sample_jaas.config sample.SampleAcn
