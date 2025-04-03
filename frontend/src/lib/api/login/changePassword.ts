import { PasswordStatusSchema, type PasswordStatus } from '$lib/schemas/response/PasswordStatus';
import { userRequest } from '$lib/api/apiClient';
import { z } from 'zod';
import { ParticipantUserSchema, type ParticipantUser } from '$lib/schemas/response/ParticipantUser';

export async function changePassword(changedPasswordUser: {
	newPassword: string;
}): Promise<PasswordStatus> {
	const changePasswordRes = await userRequest(`/api/user/change-password`, 'POST', {
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			newPassword: changedPasswordUser.newPassword
		})
	});
	return PasswordStatusSchema.parse(changePasswordRes);
}

export async function getUsers(): Promise<Array<ParticipantUser>> {
	const getUserRes = await userRequest('/api/user/users', 'GET');
	return z.array(ParticipantUserSchema).parse(getUserRes);
}
