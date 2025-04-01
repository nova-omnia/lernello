import {
	ChangePasswordStatusSchema,
	type ChangePasswordStatus
} from '$lib/models/changePasswordData';
import { userRequest } from '$lib/api/apiClient';
import { ParticipantSchema, type Participant } from '$lib/models/user';
import { z } from 'zod';

export async function changePassword(changedPasswordUser: {
	newPassword: string;
}): Promise<ChangePasswordStatus> {
	const changePasswordRes = await userRequest(`/api/user/change-password`, 'POST', {
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			newPassword: changedPasswordUser.newPassword
		})
	});
	return ChangePasswordStatusSchema.parse(changePasswordRes);
}

export async function getUsers(): Promise<Array<Participant>> {
	const getUserRes = await userRequest('/api/user/users', 'GET');
	return z.array(ParticipantSchema).parse(getUserRes);
}
