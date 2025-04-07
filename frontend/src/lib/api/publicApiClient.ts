import { createApiClient } from './createApiClient';

export const publicApiClient = createApiClient((request) => request);
