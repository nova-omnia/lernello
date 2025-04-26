import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { handleApiError } from '$lib/api/apiError';
import { fail, redirect } from '@sveltejs/kit';
import { api } from '$lib/api/apiClient';
import { CreateParticipantUserSchema } from '$lib/schemas/request/CreateParticipantUser';
import { addTrainee } from '$lib/api/collections/user';

export const load = async () => {
	const form = await superValidate(zod(CreateParticipantUserSchema));
	return { form };
};

export const actions = {
	create: handleApiError(async ({ request, fetch, params }) => {
		const form = await superValidate(request, zod(CreateParticipantUserSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		await api(fetch).req(addTrainee, form.data).parse();

		redirect(303, `/learningkit/${params.learningKitId}`);
	})
};
