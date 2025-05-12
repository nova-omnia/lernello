export interface MarkdownModificationResult {
	newContent: string;
	newSelectionStart: number;
	newSelectionEnd: number;
	preventDefault?: boolean;
}

export function handleMarkdownSyntaxInsertion(
	originalContent: string,
	selectionStart: number,
	selectionEnd: number,
	syntaxString: string,
	type: 'heading' | 'codeblock' | 'list' | 'blockquote' | 'inline'
): MarkdownModificationResult | null {
	const selectedText = originalContent.substring(selectionStart, selectionEnd);
	const placeholder = '{{selection}}';
	const placeholderIndex = syntaxString.indexOf(placeholder);

	if (placeholderIndex === -1) {
		console.error("Placeholder '{{selection}}' not found in syntax string:", syntaxString);
		return null;
	}

	const prefix = syntaxString.substring(0, placeholderIndex);
	const suffix = syntaxString.substring(placeholderIndex + placeholder.length);

	let newContentValue = '';
	let newSelectionStart = selectionStart;
	let newSelectionEnd = selectionEnd;

	if (type === 'inline' && selectedText.length > 0) {
		const textBefore = originalContent.substring(0, selectionStart);
		const textAfter = originalContent.substring(selectionEnd);
		if (textBefore.endsWith(prefix) && textAfter.startsWith(suffix)) {
			// Toggle off
			newContentValue =
				textBefore.slice(0, textBefore.length - prefix.length) +
				selectedText +
				textAfter.slice(suffix.length);
			newSelectionStart = selectionStart - prefix.length;
			newSelectionEnd = newSelectionStart + selectedText.length;
		} else {
			// Apply
			newContentValue = textBefore + prefix + selectedText + suffix + textAfter;
			newSelectionStart = selectionStart + prefix.length;
			newSelectionEnd = newSelectionStart + selectedText.length;
		}
	} else {
		// Block-level or inline without selection
		let modifiedSyntax = syntaxString.replace(placeholder, selectedText);
		let ensureNewlineBefore = false;

		if (
			type === 'heading' ||
			type === 'codeblock' ||
			((type === 'list' || type === 'blockquote') && selectedText.length === 0)
		) {
			if (selectionStart > 0 && originalContent[selectionStart - 1] !== '\n') {
				ensureNewlineBefore = true;
			}
		}

		if (ensureNewlineBefore) {
			modifiedSyntax = '\n' + modifiedSyntax;
		}

		if (type === 'codeblock' && !modifiedSyntax.endsWith('\n')) {
			modifiedSyntax += '\n';
		}

		newContentValue =
			originalContent.substring(0, selectionStart) +
			modifiedSyntax +
			originalContent.substring(selectionEnd);

		const baseOffset = ensureNewlineBefore ? 1 : 0;

		if (selectedText.length > 0) {
			const selectionInModifiedSyntaxStart = baseOffset + prefix.length;
			newSelectionStart = selectionStart + selectionInModifiedSyntaxStart;
			newSelectionEnd = newSelectionStart + selectedText.length;
		} else {
			if (type === 'codeblock') {
				// Position cursor inside the code block, after the opening ``` and newline
				const openingTag = '```\n';
				newSelectionStart = selectionStart + baseOffset + openingTag.length;
			} else {
				// Position cursor after the prefix for other types
				newSelectionStart = selectionStart + baseOffset + prefix.length;
			}
			newSelectionEnd = newSelectionStart;
		}
	}

	return {
		newContent: newContentValue,
		newSelectionStart,
		newSelectionEnd
	};
}

export function handleEnterKeyForListsAndBlockquotes(
	currentContent: string,
	selectionStart: number,
	selectionEnd: number
): MarkdownModificationResult | null {
	if (
		selectionStart !== selectionEnd ||
		(selectionStart < currentContent.length && currentContent[selectionStart] !== '\n')
	) {
		return null; // Not at the end of a line or there's a selection
	}

	const textBeforeCursor = currentContent.substring(0, selectionStart);
	const currentLineStartOffset = textBeforeCursor.lastIndexOf('\n') + 1;
	const currentLineText = textBeforeCursor.substring(currentLineStartOffset);

	let prefixToContinue: string | null = null;
	let isEmptyLinePattern = false;

	const orderedListMatch = currentLineText.match(/^(\d+)\.\s*(.*)$/);
	if (orderedListMatch) {
		const numStr = orderedListMatch[1];
		const itemContent = orderedListMatch[2];
		if (itemContent.trim() === '') {
			isEmptyLinePattern = true;
		} else {
			const nextNum = parseInt(numStr) + 1;
			prefixToContinue = `${nextNum}. `;
		}
	}

	if (!prefixToContinue && !isEmptyLinePattern) {
		const unorderedListMatch = currentLineText.match(/^(-\s+)(.*)$/);
		if (unorderedListMatch) {
			const itemContent = unorderedListMatch[2];
			if (itemContent.trim() === '') {
				isEmptyLinePattern = true;
			} else {
				prefixToContinue = '- ';
			}
		}
	}

	if (!prefixToContinue && !isEmptyLinePattern) {
		const blockquoteMatch = currentLineText.match(/^(>\s+)(.*)$/);
		if (blockquoteMatch) {
			const itemContent = blockquoteMatch[2];
			if (itemContent.trim() === '') {
				isEmptyLinePattern = true;
			} else {
				prefixToContinue = '> ';
			}
		}
	}

	if (prefixToContinue) {
		const textToInsert = '\n' + prefixToContinue;
		const newContent =
			currentContent.substring(0, selectionStart) +
			textToInsert +
			currentContent.substring(selectionEnd);
		const newCursorPos = selectionStart + textToInsert.length;
		return {
			newContent: newContent,
			newSelectionStart: newCursorPos,
			newSelectionEnd: newCursorPos,
			preventDefault: true
		};
	} else if (isEmptyLinePattern) {
		const newContent =
			currentContent.substring(0, currentLineStartOffset) +
			'\n' +
			currentContent.substring(selectionStart);
		const newCursorPos = currentLineStartOffset;
		return {
			newContent: newContent,
			newSelectionStart: newCursorPos,
			newSelectionEnd: newCursorPos,
			preventDefault: true
		};
	}

	return null;
}
