import type { Actions } from './$types';
import { createLearningUnit } from '$lib/api/learningUnits';

export const actions: Actions = {
    default: async ({ request }) => {
        const formData = await request.formData();
        const learningUnitName = formData.get('learningUnitName') as string;
        if (!learningUnitName) {
            return { error: 'Missing learning unit name' };
        }
        try {
            const createdUnit = await createLearningUnit(learningUnitName);
            return { success: true, learningUnit: createdUnit };
        } catch (error) {
            return { error: 'Failed to create learning unit' };
        }
    }
};