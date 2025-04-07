import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import { CreateLearningKitSchema } from '$lib/schemas/request/CreateLearningKit';
import { serverApiClient } from '$lib/api/serverApiClient';
import { createLearningKit } from '$lib/api/collections/learningKit';
import {
	createLearningKit,
	getLearningKit,
	updateLearningKit
} from '$lib/api/learning-kit/learningKit';
import { CreateLearningKitSchema, EditLearningKitSchema } from '$lib/models/kit';
import type { PageServerLoad } from '../../../../../.svelte-kit/types/src/routes/$types';

let editId: string | null;
export const load: PageServerLoad = async ({ url }) => {
	editId = url.searchParams.get('edit');
	let formData;

export const load = async () => {
	const form = await superValidate(zod(CreateLearningKitSchema));
	if (editId) {
		const existingKit = await getLearningKit(editId);

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
	create: handleApiError(async (event) => {
		const { request } = event;
		const form = await superValidate(request, zod(CreateLearningKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		const learningKit = await serverApiClient.req(createLearningKit, form.data);
		let learningKit;
		if (editId) {
			learningKit = await updateLearningKit({ ...form.data, uuid: editId });
		} else {
			learningKit = await createLearningKit(form.data);
		}
		const learningKitId = learningKit.uuid;

		return redirect(303, `/learningkit/${learningKitId}`);
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
