all: docker_server_make 

ubuntu_make: rest_server

ubuntu_run: 
	LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib ./rest_server

rest_server: rest_server.c
	gcc -o rest_server rest_server.c -lulfius -ljansson

call_server:
	curl -XGET localhost:8085/tictactoe

copy_needed_libs:
	mkdir -p libs
	cp /usr/local/lib/libjansson.so.4 ./libs/
	cp /usr/local/lib/libulfius.so.2.3 ./libs/
	cp /usr/local/lib/libyder.so ./libs/
	cp /usr/local/lib/liborcania.so ./libs/

docker_server_make: rest_server copy_needed_libs
	docker build -t ttt-server .

run:
	docker run -i -t -p 8085:8085 ttt-server:latest

ubuntu_clean:
	rm -rf ./rest_server 

clean:
	docker images -a | grep "ttt-server" | awk '{print $$3}' | xargs sudo docker rmi --force
	rm -rf ./rest_server ./libs
