FROM ubuntu:latest

RUN apt-get update && apt-get install -y openssh-server openjdk-11-jdk-headless python-is-python3
RUN mkdir /var/run/sshd

RUN useradd -ms /bin/bash codementor
RUN echo 'codementor:codementor12#$' | chpasswd

EXPOSE 22

CMD ["/usr/sbin/sshd", "-D"]dock