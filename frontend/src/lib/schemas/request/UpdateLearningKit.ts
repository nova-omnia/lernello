import { z } from "zod";
import { ParticipantUserSchema } from "../response/ParticipantUser";
import { FileResSchema } from "../response/FileRes";

export const UpdateLearningKitSchema = z.object({
    participant: z.array(ParticipantUserSchema).nonempty(),
    files: z.array(FileResSchema).nullable()
});