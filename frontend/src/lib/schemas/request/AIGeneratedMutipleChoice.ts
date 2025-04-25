import { z } from "zod";

export const AIGeneratedMultipleChoiceSchema = z.object({
    theoryBlockId: z.string().uuid(),
    multipleChoiceBlockId: z.string().uuid()
});

export type AIGeneratedMultipleChoice = z.infer<typeof AIGeneratedMultipleChoiceSchema>;