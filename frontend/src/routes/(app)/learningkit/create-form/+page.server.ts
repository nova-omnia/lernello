import {superValidate} from 'sveltekit-superforms';
import {zod} from 'sveltekit-superforms/adapters';
import {CreateKitSchema} from '$lib/models/kit';
import {handleApiError} from '$lib/api/apiError';
import {type Actions, fail, redirect} from '@sveltejs/kit';
import {getUsers} from '$lib/api/login/changePassword';
import {createLearningKit, getLearningKit, updateLearningKit} from "$lib/api/learning-kit/learningKit";

export const load = async ({url}: { url: any }) => {
    const editId = url.searchParams.get('edit');
    let formData;

    if (editId) {
        const existingKit = await getLearningKit(editId);

        formData = {
            uuid: existingKit.uuid,
            deadlineDate: existingKit.deadlineDate
                ? new Date(existingKit.deadlineDate)
                : undefined,
            description: existingKit.description,
            language: existingKit.language,
            name: existingKit.name,
            participants: existingKit.participants
        };
    }

    const form = await superValidate(formData, zod(CreateKitSchema));
    const users = await getUsers();
    return {form, users, editId};
};

export const actions = {
    create: handleApiError(async (event) => {
        const { request, url } = event;
        const form = await superValidate(request, zod(CreateKitSchema));
        if (!form.valid) {
            return fail(400, {form});
        }

        const editId = url.searchParams.get('edit');
        let learningKit;
        if (editId) {
            learningKit = await updateLearningKit(form.data);
        } else {
            learningKit = await createLearningKit(form.data);
        }
        const learningKitId = learningKit.uuid;

        return redirect(303, `/learningkit/${learningKitId}`);
    })
} satisfies Actions;
