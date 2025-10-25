import { defineConfig } from 'vite';
import dts from 'vite-plugin-dts';

export default defineConfig({
    build: {
        lib: {
            // Entry point of your library
            entry: 'src/query-builder.ts',
            name: 'hyperkit-query-builder',
            fileName: (format) => `hyperkit-query-builder.${format}.js`,
        },
        rollupOptions: {
            // Exclude dependencies you don't want bundled
            external: ['lodash', 'react'], // example
            output: {
                globals: {
                    lodash: '_',
                    react: 'React',
                },
            },
        },
    },
    plugins: [
        dts({
            insertTypesEntry: true, // creates a `types` entry in package.json
        }),
    ],
});
