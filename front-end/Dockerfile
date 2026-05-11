FROM node:18-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --only=dev
COPY tsconfig.json .
COPY ts ./ts
RUN npx tsc

FROM nginxinc/nginx-unprivileged:stable-alpine
COPY --from=build /app/js /usr/share/nginx/html/js
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY *.html /usr/share/nginx/html/
COPY css /usr/share/nginx/html/css
COPY assets /usr/share/nginx/html/assets
USER nginx
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
