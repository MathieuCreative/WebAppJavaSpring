import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    outDir: 'target/dist',
    assetsDir: 'static'
  },
  plugins: [vue()],
  server: {
    proxy: {
      '^/images': {
        target: 'http://localhost:8080' // Spring boot backend address
      }}},
})
