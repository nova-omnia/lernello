// theoryBlock/+page.server.ts
import type { PageServerLoad } from './$types';
import { carta } from '$lib/carta';

export const load: PageServerLoad = async () => {

  const markdownContent = `
# Welcome to the **Theory Block** 🎉

## Lirum Ipsum Adventures 🚀

> "Lirum ipsum dolor sit amet, consectetur adipiscing elit.  
> Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."  
> — *Ancient Proverb*

### Fun Facts 🧠
- Lirum ipsum is not just placeholder text, it's a **lifestyle**.
- It has been used since the **1500s** to confuse designers.
- Even aliens 🛸 use it to format their intergalactic documents.

---

### A Mathematical Journey 📐

Here’s a quick equation for your amusement:

$$
E = mc^2 + \\text{Lirum Ipsum Factor} \\cdot \\pi
$$

And don’t forget this classic:

$$
\\int_{0}^{\\infty} \\frac{1}{x^2} dx = \\text{Oops! Undefined!}
$$

---

### Code Snippet 💻

\`\`\`javascript
function lirumIpsum() {
  console.log("Lirum ipsum dolor sit amet!");
}
lirumIpsum();
\`\`\`

---

### Random Table 📊

| Lirum | Ipsum | Dolor |
|-------|-------|-------|
| Foo   | Bar   | Baz   |
| 42    | 3.14  | ∞     |

---

### Closing Thoughts 🤔

Remember, **lirum ipsum** is not just text — it's a way of life.  
*Stay curious, stay lorem!*

---

*Generated with ❤️ by the Theory Block.*
`;

  const renderedHtml = await carta.render(markdownContent);

  return {
    html: renderedHtml
  };
};
