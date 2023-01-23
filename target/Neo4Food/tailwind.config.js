/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./WEB-INF/jsp/**/*.{html,js,jsp}",
            "./jsp/**/*.{html,js,jsp}",
            "./*.{html,js,jsp}"],
  theme: {
      colors: {
          'principale': '#FFF4EA',
          'test_col' : '#7C2714'
      },
    extend: {},
  },
  plugins: [],
}
