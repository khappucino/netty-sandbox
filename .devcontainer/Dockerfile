#-------------------------------------------------------------------------------------------------------------
# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License. See https://go.microsoft.com/fwlink/?linkid=2090316 for license information.
#-------------------------------------------------------------------------------------------------------------

FROM gradle:5.4.1-jdk11

# Configure apt
ENV DEBIAN_FRONTEND=noninteractive
RUN apt update \
  && apt -y install --no-install-recommends apt-utils 2>&1
RUN apt install -y \
  apt-transport-https \
  ca-certificates \
  curl \
  gnupg-agent \
  software-properties-common \
  lsb-release \
  && curl -fsSL https://download.docker.com/linux/$(lsb_release -is | tr '[:upper:]' '[:lower:]')/gpg | apt-key add - 2>/dev/null \
  && add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/$(lsb_release -is | tr '[:upper:]' '[:lower:]') $(lsb_release -cs) stable" \
  && apt update

RUN apt -y install \
  git \
  procps \
  clang \
  docker-ce-cli \
  net-tools \
  shellcheck \
  python3 \
  python3-pip

# Install pylint, yapf
RUN pip3 install pylint yapf

# Clean up
RUN apt autoremove -y \
  && apt clean -y \
  && rm -rf /var/lib/apt/lists/*
ENV DEBIAN_FRONTEND=dialog

# Allow for a consistant java home location for settings - image is changing over time
RUN if [ ! -d "/docker-java-home" ]; then ln -s "${JAVA_HOME}" /docker-java-home; fi

# Set the default shell to bash instead of sh
ENV SHELL /bin/bash

