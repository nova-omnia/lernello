import { FileResSchema } from '$lib/schemas/response/FileRes';
import { MultipartFormDataSchema } from '$lib/schemas/request/MultipartFormDataSchema';
import { createEndpoint } from '../createEndpoint';
import { z } from 'zod';

const REQUEST_MAPPING = '/api/files';

export const uploadFile = createEndpoint({
    method: 'POST',
    getPath: () => `${REQUEST_MAPPING}/upload`,
    payload: {
        schema: z.null(),
        defaultValidate: false
    },
    response: {
        schema: FileResSchema,
        defaultValidate: true
    }
});

export const getById = createEndpoint({
    method: 'GET',
    getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
    payload: {
        schema: z.null(),
        defaultValidate: false
    },
    response: {
        schema: FileResSchema,
        defaultValidate: true
    }
});

export const deleteFile = createEndpoint({
    method: 'DELETE',
    getPath: (id: string) => `${REQUEST_MAPPING}/${id}`,
    payload: {
        schema: z.null(),
        defaultValidate: false
    },
    response: {
        schema: z.null(),
        defaultValidate: true
    }
});
export const getAllFiles = createEndpoint({
    method: 'GET',
    getPath: () => `${REQUEST_MAPPING}/all`,
    payload: {
        schema: z.null(),
        defaultValidate: false
    },
    response: {
        schema: FileResSchema.array(),
        defaultValidate: true
    }
});

// export const getBinaryFile = createEndpoint({
//     method: 'GET',
//     getPath: (id: string) => `${REQUEST_MAPPING}/binary/${id}`,
//     payload: {
//         schema: z.null(),
//         defaultValidate: false
//     },
//     response: {
//         schema: z.instanceof(Blob),
//         defaultValidate: true
//     }
// });