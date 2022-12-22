echo "Building docker image and publishing"

read -p "Version:" version
echo version=$version
read -s -p "Please, enter, Maven repo password:" repoPassword
docker build --build-arg OCTAGON_MAVEN_REPO_PASSWORD=$repoPassword -t nanobootorg/octagon:$version --label nanobootorg/octagon:$version .
#docker image prune --force --filter='label=nanobootorg/octagon:$version'
docker push nanobootorg/octagon:$version
echo done
