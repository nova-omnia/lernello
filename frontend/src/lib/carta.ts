import { Carta } from 'carta-md';
import DOMPurify from 'isomorphic-dompurify';

export const carta = new Carta({
  sanitizer: DOMPurify.sanitize
});
