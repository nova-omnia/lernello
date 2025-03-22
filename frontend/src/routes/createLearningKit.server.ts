import {superValidate} from "sveltekit-superforms";
import {zod} from "sveltekit-superforms/adapters";
import {CreateKitSchema} from "$lib/models/user";
import {handleApiError} from "$lib/api/apiError";
import {type Actions, fail} from "@sveltejs/kit";

export const load = async () => {
    const form = await superValidate(zod(CreateKitSchema));
    return { form };
};

export const actions = {
    login: handleApiError(async ({ request }) => {
        const form = await superValidate(request, zod(CreateKitSchema));
        if (!form.valid) {
            return fail(400, {form});
        }
    })
}satisfies Actions;