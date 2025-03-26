import { Carta } from 'carta-md';
import { attachment } from '@cartamd/plugin-attachment';
import { emoji } from '@cartamd/plugin-emoji';
import { slash } from '@cartamd/plugin-slash';
import { code } from '@cartamd/plugin-code';
import DOMPurify from 'isomorphic-dompurify';


export const carta = new Carta({
	sanitizer: DOMPurify.sanitize,
    extensions: [
        emoji(),
        slash(),
        code()
    ]
});
