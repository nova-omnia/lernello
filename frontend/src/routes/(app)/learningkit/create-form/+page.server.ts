import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import {
	CreateLearningKitSchema,
	EditLearningKitSchema
} from '$lib/schemas/request/CreateLearningKit';
import { serverApiClient } from '$lib/api/serverApiClient';
import {
	createLearningKit,
	editLearningKit,
	getLearningKitById
} from '$lib/api/collections/learningKit';
import type { PageServerLoad } from '../../../../../.svelte-kit/types/src/routes/$types';
import { LearningKitResSchema } from '$lib/schemas/response/LearningKitRes';

let editId: string | null;
export const load: PageServerLoad = async ({ url }) => {
	editId = url.searchParams.get('edit');
	let formData;

	if (editId) {
		const response = await serverApiClient.req(getLearningKitById, null, editId);
		const existingKit = LearningKitResSchema.parse(response);

		formData = {
			uuid: existingKit.uuid,
			deadlineDate: existingKit.deadlineDate
				? formatLocalDateTime(new Date(existingKit.deadlineDate))
				: undefined,
			description: existingKit.description,
			name: existingKit.name,
			context: existingKit.context
		};
	}

	const form = await superValidate(formData, zod(EditLearningKitSchema));
	return { form, editId };
};

export const actions = {
	create: handleApiError(async ({ request }) => {
		const form = await superValidate(request, zod(CreateLearningKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		let learningKit;
		if (editId) {
			learningKit = await serverApiClient.req(editLearningKit, {
				...form.data,
				uuid: editId,
				deadlineDate: form.data.deadlineDate
					? form.data.deadlineDate.toISOString().split('T')[0]
					: null
			});
		} else {
			learningKit = await serverApiClient.req(createLearningKit, {
				...form.data,
				deadlineDate: form.data.deadlineDate
					? form.data.deadlineDate.toISOString().split('T')[0]
					: null
			});
		}

		return redirect(303, `/learningkit/${learningKit.uuid}`);
	})
} satisfies Actions;

function formatLocalDateTime(date: Date): string {
	const pad = (n: number) => n.toString().padStart(2, '0');

	const year = date.getFullYear();
	const month = pad(date.getMonth() + 1);
	const day = pad(date.getDate());
	const hours = pad(date.getHours());
	const minutes = pad(date.getMinutes());

	return `${year}-${month}-${day}T${hours}:${minutes}`;
}
