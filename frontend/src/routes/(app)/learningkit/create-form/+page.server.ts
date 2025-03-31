import { superValidate } from 'sveltekit-superforms';
import { zod } from 'sveltekit-superforms/adapters';
import { CreateKitSchema } from '$lib/models/kit';
import { handleApiError } from '$lib/api/apiError';
import { type Actions, fail, redirect } from '@sveltejs/kit';
import { getUsers } from '$lib/api/login/changePassword';

export const load = async () => {
	const form = await superValidate(zod(CreateKitSchema));
	const users = await getUsers();
	return { form, users };
};

export const actions = {
	create: handleApiError(async ({ request }) => {
		const form = await superValidate(request, zod(CreateKitSchema));
		if (!form.valid) {
			return fail(400, { form });
		}

		// ToDo: Use for redirect to the created learning kit
		// const learningKit = await createLearningKit(form.data);
		// const learningKitId = learningKit.uuid;

		return redirect(303, `/dashboard`);
	})
} satisfies Actions;
