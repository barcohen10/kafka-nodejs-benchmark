module.exports = {
  extends: ["airbnb-base", "airbnb-typescript/base"],
  parser: "@typescript-eslint/parser",
  ignorePatterns: ["dist/**/**"],
  parserOptions: {
    createDefaultProgram: true,
    sourceType: "module",
    project: "./tsconfig.json",
  },
  plugins: ["@typescript-eslint", "simple-import-sort"],
  overrides: [
    {
      files: "*",
      rules: {
        "simple-import-sort/imports": "error",
        "simple-import-sort/exports": "error",
        "no-console": "off",
      },
    },
  ],
};
