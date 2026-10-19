FROM nginx:alpine

ENV BACKEND_URL=http://host.docker.internal:8080

COPY index.html chamados.html /usr/share/nginx/html/
COPY js/ /usr/share/nginx/html/js/
COPY styles/ /usr/share/nginx/html/styles/
COPY nginx.conf.template /etc/nginx/templates/default.conf.template

EXPOSE 80
