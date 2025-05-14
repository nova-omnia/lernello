classDiagram
direction BT
class AIBlockRestController {
  - AIBlockService aiBlockService
  - BlockMapper blockMapper
  + generateTheory(AIGeneratedTheoryBlockRequest) TheoryBlockResDTO
  + generateMultipleChoice(AIGeneratedQuestionBlockRequest) MultipleChoiceBlockResDTO
  + generateQuestion(AIGeneratedQuestionBlockRequest) QuestionBlockResDTO
}
class AIBlockService {
  - FileService fileService
  - BlockRepository blockRepository
  - AIClient aiClient
  - ObjectMapper objectMapper
  - ExecutorService executor
  - extractTopicsFromFiles(List~UUID~, ConcurrentHashMap~String, List~Block~~) void
  - generateTheoryBlockFromTopic(String, String) TheoryBlock
  - parseJson(String, Class~T~, String) T
  - generateBlocksFromTopics(JsonNode, ConcurrentHashMap~String, List~Block~~) void
  - createBlocksForTopic(String, List~Block~, String) void
  + checkQuestionAnswerWithAI(String, String) boolean
  - generateMultipleChoiceBlockAIFromTopic(String, String) MultipleChoiceBlock
  + generateQuestionBlockAI(UUID, UUID) QuestionBlock
  - generateTranslationsParallel(QuestionBlock) void
  - generateTranslationsParallel(MultipleChoiceBlock) void
  - generateTranslationsParallel(Block, String) void
  + generateBlocksAI(List~UUID~) List~Block~
  + generateTheoryBlockAI(UUID, List~UUID~, String) TheoryBlock
  - parseJsonTree(String, String) JsonNode
  - generateQuestionBlockAIFromTopic(String, String) QuestionBlock
  + generateMultipleChoiceBlockAI(UUID, UUID) MultipleChoiceBlock
}
class AIClient {
  - RestTemplate restTemplate
  - String apiUrl
  - String apiKey
  + sendPrompt(String) String
  - extractJson(String) String
}
class AIGeneratedQuestionBlockRequest {
  - UUID blockId
  - UUID theoryBlockId
  + blockId() UUID
  + theoryBlockId() UUID
}
class AIGeneratedTheoryBlockRequest {
  - String topic
  - UUID blockId
  - List~UUID~ files
  + files() List~UUID~
  + topic() String
  + blockId() UUID
}
class AIPromptTemplate {
<<enumeration>>
  +  MULTIPLE_CHOICE
  +  TOPIC_EXTRACTION
  +  QUESTION_BLOCK
  +  TRANSLATION
  - String template
  +  THEORY_BLOCK
  +  CHECK_ANSWER
  + values() AIPromptTemplate[]
  + format(Object[]) String
  + valueOf(String) AIPromptTemplate
}
class AddBlockActionDTO {
  - Integer index
  - String blockId
  - BlockActionType type
  - CreateBlockDTO data
  + type() BlockActionType
  + data() CreateBlockDTO
  + index() Integer
  + blockId() String
}
class AuthEntryPointJwt {
  + commence(HttpServletRequest, HttpServletResponse, AuthenticationException) void
}
class AuthRestController {
  - UserService userService
  - UserLoginMapper userLoginMapper
  + signin(UserLoginDTO) LoggedInUserDTO
}
class AuthTokenFilter {
  - CustomUserDetailsService userDetailsService
  - JwtUtil jwtUtils
  # doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain) void
  - parseJwt(HttpServletRequest) String
}
class Block {
  - String name
  - int position
  - UUID uuid
  - LearningUnit learningUnit
  - BlockType type
  # canEqual(Object) boolean
  + getPosition() int
  + equals(Object) boolean
  + getLearningUnit() LearningUnit
  + getType() BlockType
  + setType(BlockType) void
  + toString() String
  + setPosition(int) void
  + getName() String
  + hashCode() int
  + setLearningUnit(LearningUnit) void
  + setUuid(UUID) void
  + getUuid() UUID
  + setName(String) void
}
class BlockActionDTO {
<<Interface>>

}
class BlockActionType {
<<enumeration>>
  +  UPDATE_BLOCK
  +  UPDATE_BLOCK_NAME
  +  REORDER_BLOCK
  +  ADD_BLOCK
  +  REMOVE_BLOCK
  + values() BlockActionType[]
  + valueOf(String) BlockActionType
}
class BlockLanguage {
<<enumeration>>
  +  FRENCH
  +  GERMAN
  +  ITALIAN
  + values() BlockLanguage[]
  + valueOf(String) BlockLanguage
}
class BlockMapper {
<<Interface>>
  + toMultipleChoiceBlockResDTO(MultipleChoiceBlock) MultipleChoiceBlockResDTO
  + toQuestionBlockResDTO(QuestionBlock) QuestionBlockResDTO
  + toTheoryBlock(TheoryBlockResDTO) TheoryBlock
  + toTheoryBlockResDTO(TheoryBlock) TheoryBlockResDTO
  + toBlockResDTO(Block) BlockResDTO
}
class BlockMapperImpl {
  + toMultipleChoiceBlockResDTO(MultipleChoiceBlock) MultipleChoiceBlockResDTO
  + toQuestionBlockResDTO(QuestionBlock) QuestionBlockResDTO
  + toTheoryBlock(TheoryBlockResDTO) TheoryBlock
  + toTheoryBlockResDTO(TheoryBlock) TheoryBlockResDTO
}
class BlockProgress {
  - User user
  - UUID uuid
  - Block block
  - LearningUnitProgress learningUnitProgress
  # canEqual(Object) boolean
  + getBlock() Block
  + getUuid() UUID
  + hashCode() int
  + setUser(User) void
  + getLearningUnitProgress() LearningUnitProgress
  + setLearningUnitProgress(LearningUnitProgress) void
  + setBlock(Block) void
  + toString() String
  + getUser() User
  + equals(Object) boolean
  + setUuid(UUID) void
}
class BlockProgressRepository {
<<Interface>>
  + findByLearningUnitProgress_Uuid(UUID) List~BlockProgress~
  + findByUserAndBlock(User, Block) Optional~BlockProgress~
  + findByUser_UuidAndBlock_Uuid(UUID, UUID) Optional~BlockProgress~
  + findByBlock_Uuid(UUID) List~BlockProgress~
}
class BlockProgressResDTO {
<<Interface>>
  + blockType() BlockType
  + blockId() UUID
}
class BlockRepository {
<<Interface>>
  + findBlocksAsc(UUID) List~Block~
  + updatePosition(int, UUID) void
}
class BlockResDTO {
<<Interface>>

}
class BlockService {
  - Map~String, UUID~ temporaryKeyMap
  - BlockProgressRepository blockProgressRepository
  - LearningUnitRepository learningUnitRepository
  - BlockRepository blockRepository
  - updateBlock(LearningUnit, UpdateBlockActionDTO) void
  - updateBlockName(LearningUnit, UpdateBlockNameActionDTO) void
  - removeBlock(LearningUnit, RemoveBlockActionDTO) void
  + applyBlockActions(UUID, List~BlockActionDTO~) Map~String, UUID~
  - reorderBlocks(LearningUnit, ReorderBlockActionDTO) void
  - addBlock(LearningUnit, AddBlockActionDTO) void
  - getActionKey(BlockActionDTO) String
  - filterCorrelatedActions(List~BlockActionDTO~) List~BlockActionDTO~
  - isTemporaryId(String) boolean
}
class BlockTest
class BlockType {
<<enumeration>>
  +  QUESTION
  +  THEORY
  +  MULTIPLE_CHOICE
  + values() BlockType[]
  + valueOf(String) BlockType
}
class Build_gradle {
  + main(String[]) Unit
}
class ChangePasswordDataDTO {
  - String newPassword
  + newPassword() String
}
class ChatRequest {
  - double temperature
  - String model
  - List~Map~String, String~~ messages
  - double frequency_penalty
  + getMessages() List~Map~String, String~~
  + getFrequency_penalty() double
  + getTemperature() double
  + getModel() String
}
class ChatResponse {
  - List~Choice~ choices
  + getChoices() List~Choice~
}
class CheckMultipleChoiceAnswerDTO {
  - List~String~ answers
  - UUID blockId
  + answers() List~String~
  + blockId() UUID
}
class CheckQuestionAnswerDTO {
  - String answer
  - UUID blockId
  + blockId() UUID
  + answer() String
}
class CreateBlockDTO {
<<Interface>>

}
class CreateFolderDTO {
  - String name
  + name() String
}
class CreateLearningKitDTO {
  - ZonedDateTime deadlineDate
  - String description
  - String context
  - String name
  + deadlineDate() ZonedDateTime
  + description() String
  + context() String
  + name() String
}
class CreateLearningUnitDTO {
  - String name
  - UUID learningKitId
  + learningKitId() UUID
  + name() String
}
class CreateMultipleChoiceBlockDTO {
  - BlockType type
  - List~String~ possibleAnswers
  - String question
  - List~String~ correctAnswers
  - String name
  - int position
  + name() String
  + position() int
  + possibleAnswers() List~String~
  + correctAnswers() List~String~
  + type() BlockType
  + question() String
}
class CreateParticipantDTO {
  - String username
  - String name
  - String surname
  + username() String
  + name() String
  + surname() String
}
class CreateQuestionBlockDTO {
  - String expectedAnswer
  - BlockType type
  - int position
  - String name
  - String question
  + expectedAnswer() String
  + position() int
  + type() BlockType
  + question() String
  + name() String
}
class CreateTheoryBlockDTO {
  - BlockType type
  - int position
  - String name
  - String content
  + content() String
  + type() BlockType
  + name() String
  + position() int
}
class CreateUserDTO {
  - String name
  - String surname
  - String username
  - Role role
  + surname() String
  + username() String
  + name() String
  + role() Role
}
class CustomUserDetailsService {
  - UserRepository userRepository
  + loadUserByUsername(String) UserDetails
  - getUserScopes(User) List~GrantedAuthority~
  + getUserIdByUsername(String) UUID
}
class EmailService {
  - JavaMailSender javaMailSender
  - UserRepository userRepository
  - PasswordEncoder passwordEncoder
  - UserService userService
  - String sender
  - Logger log
  - String CHARSET
  + sendLearningKitInvitation(User, LearningKit) void
  - buildResetPasswordHtmlContent(String, String) String
  + sendNewLoginData(User) void
  - baseHtmlWrapper(String) String
  - buildLearningKitTextContent(User, LearningKit, String) String
  - builtResetPasswordTextContent(String, String) String
  - buildMimeMessage(String, String, String, String) MimeMessage
  - buildLearningKitHtmlContent(User, LearningKit, String) String
}
class EntityEx {
  - String manufacturer
  - List~Double~ price
  - Integer quantity
  - String description
  - Long id
  - String name
  + setQuantity(Integer) void
  + hashCode() int
  + getName() String
  + equals(Object) boolean
  + setName(String) void
  + getDescription() String
  # canEqual(Object) boolean
  + getManufacturer() String
  + setPrice(List~Double~) void
  + getId() Long
  + setManufacturer(String) void
  + getQuantity() Integer
  + setId(Long) void
  + getPrice() List~Double~
  + toString() String
  + setDescription(String) void
}
class EntityMapper {
<<Interface>>
  + update(EntityUpdateDTO, EntityEx) void
  + priceListToString(List~Double~) List~String~
}
class EntityMapperImpl {
  - JsonNullableMapper jsonNullableMapper
  + update(EntityUpdateDTO, EntityEx) void
  # stringListToDoubleList(List~String~) List~Double~
}
class EntityUpdateDTO {
  - String name
  - List~String~ price
  - Integer quantity
  - JsonNullable~String~ description
  - JsonNullable~String~ manufacturer
  + price() List~String~
  + manufacturer() JsonNullable~String~
  + name() String
  + quantity() Integer
  + description() JsonNullable~String~
}
class File {
  - String name
  - UUID uuid
  + setUuid(UUID) void
  # canEqual(Object) boolean
  + hashCode() int
  + toString() String
  + setName(String) void
  + getUuid() UUID
  + getName() String
  + equals(Object) boolean
}
class FileMapper {
<<Interface>>
  + toDTO(File) FileResDTO
}
class FileMapperImpl {
  + toDTO(File) FileResDTO
}
class FileRepository {
<<Interface>>
  + findByName(String) Optional~File~
}
class FileResDTO {
  - String name
  - UUID uuid
  + name() String
  + uuid() UUID
}
class FileRestController {
  - FileMapper fileMapper
  - FileSystemService fileService
  + uploadFile(MultipartFile) FileResDTO
  + getFileById(UUID) Optional~FileResDTO~
  + getAllFiles() List~FileResDTO~
  + deleteFile(UUID) UUID
}
class FileService {
<<Interface>>
  + findAll() List~File~
  + findById(UUID) Optional~File~
  + extractTextFromFile(UUID) String
  + findAllByIds(List~UUID~) List~File~
  + extractTextFromFiles(List~UUID~) String
  + deleteById(UUID) void
  + save(MultipartFile) File
}
class FileSystemService {
  - FileRepository fileRepository
  - String storagePath
  - LearningKitRepository learningKitRepository
  + findById(UUID) Optional~File~
  - removeFileFromAllLearningKits(File) void
  + findAll() List~File~
  + extractTextFromFile(UUID) String
  + findAllByIds(List~UUID~) List~File~
  + extractTextFromFiles(List~UUID~) String
  - loadFileAsStream(UUID) InputStream
  - resolveUniqueFileName(String) String
  + deleteById(UUID) void
  + save(MultipartFile) File
}
class FileTest {
  - Validator validator
  - FileRepository fileRepository
  - File testFile
  - String storagePath
  + testNameConstraints() void
  - assertConstraintViolation(Set~ConstraintViolation~File~~, String, Class~?~) void
  + testFileCreationWithName() void
  + setUpValidator() void
  + testUUIDNotNull() void
  + setUp() void
  + testFileSave() void
  + testNameUnique() void
}
class Folder {
  - UUID uuid
  - String name
  - List~Folder~ subFolders
  - Folder parentFolder
  + setUuid(UUID) void
  + toString() String
  + getSubFolders() List~Folder~
  - isCircularReference(Folder) boolean
  + getUuid() UUID
  + getName() String
  + setParentFolder(Folder) void
  + getParentFolder() Folder
  + setSubFolders(List~Folder~) void
  + setName(String) void
  + hashCode() int
  + equals(Object) boolean
  # canEqual(Object) boolean
}
class FolderMapper {
<<Interface>>
  + toDTO(Folder) FolderResDTO
  + toEntity(CreateFolderDTO) Folder
}
class FolderMapperImpl {
  - ParentFolderMapper parentFolderMapper
  + toDTO(Folder) FolderResDTO
  # folderToSubFolderResDTO(Folder) SubFolderResDTO
  # folderListToSubFolderResDTOList(List~Folder~) List~SubFolderResDTO~
  + toEntity(CreateFolderDTO) Folder
}
class FolderRepository {
<<Interface>>

}
class FolderResDTO {
  - List~SubFolderResDTO~ subFolders
  - UUID uuid
  - String name
  - ParentFolderResDTO parentFolder
  + parentFolder() ParentFolderResDTO
  + subFolders() List~SubFolderResDTO~
  + uuid() UUID
  + name() String
}
class FolderRestController {
  - FolderService folderService
  - FolderMapper folderMapper
  + getAllFolders() List~FolderResDTO~
  + createFolder(CreateFolderDTO) FolderResDTO
  + getFolderById(UUID) Optional~FolderResDTO~
}
class FolderService {
  - FolderRepository folderRepository
  + deleteById(UUID) void
  + findById(UUID) Optional~Folder~
  + findAll() List~Folder~
  + save(Folder) Folder
}
class FolderTest {
  - Folder parentFolder
  - FolderRepository folderRepository
  - Folder rootFolder
  - Validator validator
  + testOrphanRemoval() void
  + testNullParentFolder() void
  - assertConstraintViolation(Set~ConstraintViolation~Folder~~, String, Class~?~) void
  + testFolderCreationWithName() void
  + testFolderUuidGeneration() void
  + testDeepHierarchy() void
  + testEmptySubFoldersAndLearningKits() void
  + testFolderCreationWithParentFolder() void
  + setUp() void
  + testFolderNameConstraints() void
  + testSubFoldersAssociation() void
  + testCircularRelationship() void
  + testValidationOnUpdate() void
  + setUpValidator() void
}
class GenerateLearningUnitDTO {
  - List~UUID~ fileIds
  + fileIds() List~UUID~
}
class GenericSuccessDTO {
  - boolean success
  + success() boolean
}
class JacksonConfig {
  ~ customizer() Jackson2ObjectMapperBuilderCustomizer
}
class JacksonConfigTests {
  - EntityMapper mapper
  ~ shouldNotUpdateAnyFieldInEntityEx() void
  ~ shouldUpdateAllEntitiesInEntityExExceptId() void
  ~ shouldNotUpdateAnyFieldInEntityEx_2() void
  ~ shouldUpdateOnlyNullableFieldsInEntityEx() void
}
class JpaAuditingConfig {
  + dateTimeProvider() DateTimeProvider
}
class JsonNullableMapper {
<<Interface>>
  + wrap(T) JsonNullable~T~
  + unwrap(JsonNullable~T~) T
  + isPresent(JsonNullable~T~) boolean
}
class JsonNullableMapperImpl
class JwtUtil {
  - int jwtExpirationMs
  - SecretKey key
  - String jwtSecret
  + generateToken(String) String
  + getExpirationTime() Duration
  + validateJwtToken(String) boolean
  + init() void
  + getUsernameFromToken(String) String
}
class LearningKit {
  - boolean published
  - String name
  - List~LearningUnit~ learningUnits
  - List~File~ files
  - int completionRate
  - List~User~ participants
  - String context
  - String description
  - ZonedDateTime deadlineDate
  - LocalDateTime createdAt
  - UUID uuid
  - int averageProgress
  + hashCode() int
  + setUuid(UUID) void
  + setCompletionRate(int) void
  + setParticipants(List~User~) void
  + setLearningUnits(List~LearningUnit~) void
  + getCompletionRate() int
  + setPublished(boolean) void
  + setAverageProgress(int) void
  + getDescription() String
  + equals(Object) boolean
  + toString() String
  + getName() String
  + getUuid() UUID
  + getContext() String
  + setContext(String) void
  + getParticipants() List~User~
  + setFiles(List~File~) void
  + setCreatedAt(LocalDateTime) void
  # canEqual(Object) boolean
  + setName(String) void
  + isPublished() boolean
  + setDeadlineDate(ZonedDateTime) void
  + getLearningUnits() List~LearningUnit~
  + getAverageProgress() int
  + getFiles() List~File~
  + getCreatedAt() LocalDateTime
  + getDeadlineDate() ZonedDateTime
  + setDescription(String) void
}
class LearningKitMapper {
<<Interface>>
  + update(UpdateLearningKitDTO, LearningKit) void
  + toDTO(LearningKit) LearningKitResDTO
  + toEntity(CreateLearningKitDTO) LearningKit
}
class LearningKitMapperImpl {
  - JsonNullableMapper jsonNullableMapper
  - UserService userService
  - LearningUnitMapper learningUnitMapper
  - FileService fileService
  + toDTO(LearningKit) LearningKitResDTO
  + toEntity(CreateLearningKitDTO) LearningKit
  # fileListToFileResDTOList(List~File~) List~FileResDTO~
  # userToParticipantUserDTO(User) ParticipantUserDTO
  # fileToFileResDTO(File) FileResDTO
  + update(UpdateLearningKitDTO, LearningKit) void
  # learningUnitListToLearningUnitResDTOList(List~LearningUnit~) List~LearningUnitResDTO~
  # userListToParticipantUserDTOList(List~User~) List~ParticipantUserDTO~
}
class LearningKitOpened {
  - UUID learningKitId
  + learningKitId() UUID
}
class LearningKitProgress {
  - ZonedDateTime lastOpenedAt
  - User user
  - LearningKit learningKit
  - boolean completed
  - boolean opened
  - List~LearningUnitProgress~ learningUnitProgresses
  - ZonedDateTime completedAt
  - int progressPercentage
  - ZonedDateTime firstOpenedAt
  - UUID uuid
  + getLearningKit() LearningKit
  + setLearningUnitProgresses(List~LearningUnitProgress~) void
  + setCompletedAt(ZonedDateTime) void
  + getLearningUnitProgresses() List~LearningUnitProgress~
  + addLearningUnitProgress(LearningUnitProgress) void
  + hashCode() int
  # canEqual(Object) boolean
  + toString() String
  + setLearningKit(LearningKit) void
  + equals(Object) boolean
  + removeLearningUnitProgress(LearningUnitProgress) void
  + setProgressPercentage(int) void
  + isOpened() boolean
  + getProgressPercentage() int
  + getLastOpenedAt() ZonedDateTime
  + getCompletedAt() ZonedDateTime
  + setUuid(UUID) void
  + setOpened(boolean) void
  + setFirstOpenedAt(ZonedDateTime) void
  + getUuid() UUID
  + setLastOpenedAt(ZonedDateTime) void
  + isCompleted() boolean
  + getUser() User
  + setUser(User) void
  + setCompleted(boolean) void
  + getFirstOpenedAt() ZonedDateTime
}
class LearningKitProgressRepository {
<<Interface>>
  + findByUserAndLearningKit(User, LearningKit) Optional~LearningKitProgress~
  + findByUser_UuidAndLearningKit_Uuid(UUID, UUID) Optional~LearningKitProgress~
  + findAllByLearningKit_Uuid(UUID) List~LearningKitProgress~
}
class LearningKitProgressResDTO {
  - ZonedDateTime completedAt
  - ZonedDateTime firstOpenedAt
  - UUID userId
  - int progressPercentage
  - boolean isOpened
  - ZonedDateTime lastOpenedAt
  - String learningKitId
  - boolean isCompleted
  + isOpened() boolean
  + completedAt() ZonedDateTime
  + lastOpenedAt() ZonedDateTime
  + userId() UUID
  + isCompleted() boolean
  + progressPercentage() int
  + firstOpenedAt() ZonedDateTime
  + learningKitId() String
}
class LearningKitRepository {
<<Interface>>
  + findAllByFilesContains(File) List~LearningKit~
  + findAllByParticipantsContains(User) List~LearningKit~
  + findAllByOrderByCreatedAtDesc(Pageable) Page~LearningKit~
  + findAllByParticipants_UuidAndPublishedTrue(UUID, Pageable) Page~LearningKit~
}
class LearningKitResDTO {
  - String description
  - UUID uuid
  - boolean published
  - List~LearningUnitResDTO~ learningUnits
  - ZonedDateTime deadlineDate
  - int averageProgress
  - String name
  - String context
  - int completionRate
  - List~ParticipantUserDTO~ participants
  - List~FileResDTO~ files
  + name() String
  + deadlineDate() ZonedDateTime
  + context() String
  + learningUnits() List~LearningUnitResDTO~
  + files() List~FileResDTO~
  + averageProgress() int
  + description() String
  + uuid() UUID
  + published() boolean
  + completionRate() int
  + participants() List~ParticipantUserDTO~
}
class LearningKitRestController {
  - LearningUnitService learningUnitService
  - LearningKitService learningKitService
  - UserService userService
  - LearningKitMapper learningKitMapper
  - LearningKitProgressRepository learningKitProgressRepository
  - CustomUserDetailsService customUserDetailsService
  + deleteLearningKit(UUID) UUID
  + getList(UserDetails, Pageable) Page~LearningKitResDTO~
  + updateLearningKit(UpdateLearningKitDTO, UUID) LearningKitResDTO
  + getLearningKitById(UUID) LearningKitResDTO
  + addTrainee(UUID, CreateParticipantDTO) UUID
  + createLearningKit(CreateLearningKitDTO) LearningKitResDTO
  + reorderLearningUnits(UUID, UpdateLearningUnitOrderDTO) GenericSuccessDTO
  + removeParticipantFromKit(UUID, UUID) UUID
  + publishLearningKit(UUID) UUID
}
class LearningKitService {
  - LearningKitRepository learningKitRepository
  - EmailService emailService
  - ProgressService progressService
  - UserRepository userRepository
  - updateLearningKitAverageProgress(LearningKit) void
  + publishLearningKit(UUID) void
  + saveTraineeInKit(UUID, User) void
  - isParticipant(UUID) boolean
  + reorderLearningUnits(LearningKit, String[]) void
  - updateLearningKitCompletionRate(LearningKit) void
  + save(LearningKit) LearningKit
  + removeParticipant(UUID, UUID) void
  + getList(Pageable, UUID) Page~LearningKit~
  + findById(UUID) Optional~LearningKit~
  + deleteById(UUID) void
}
class LearningKitTest {
  - Validator validator
  - LearningKitRepository learningKitRepository
  + testLearningKitUuidGeneration() void
  - assertConstraintViolation(Set~ConstraintViolation~LearningKit~~, String, Class~?~) void
  + testLearningKitNameConstraints() void
  + setUpValidator() void
  + testLearningKitCreationWithName() void
  + testLearningUnitsAssociation() void
  + testOrphanRemovalForLearningUnits() void
  + testValidationOnUpdate() void
}
class LearningUnit {
  - List~Block~ blocks
  - UUID uuid
  - LearningKit learningKit
  - String name
  - int position
  + toString() String
  # canEqual(Object) boolean
  + getUuid() UUID
  + getBlocks() List~Block~
  + getPosition() int
  + setLearningKit(LearningKit) void
  + setPosition(int) void
  + getName() String
  + hashCode() int
  + setUuid(UUID) void
  + setBlocks(List~Block~) void
  + equals(Object) boolean
  + getLearningKit() LearningKit
  + setName(String) void
}
class LearningUnitMapper {
  - TranslatedBlockRepository translatedBlockRepository
  + toEntity(CreateLearningUnitDTO, LearningKit) LearningUnit
  + mapTranslatedBlocks(List~TranslatedBlock~) List~TranslatedBlockResDTO~
  + toDTO(LearningUnit) LearningUnitResDTO
  + toTranslatedDto(TranslatedBlock) TranslatedBlockResDTO
  + mapBlockToResDTO(Block) BlockResDTO
}
class LearningUnitOpenedDTO {
  - UUID learningUnitId
  + learningUnitId() UUID
}
class LearningUnitProgress {
  - User user
  - boolean completed
  - ZonedDateTime lastOpenedAt
  - List~BlockProgress~ userBlockProgresses
  - ZonedDateTime firstOpenedAt
  - UUID uuid
  - LearningKitProgress learningKitProgress
  - boolean opened
  - int progressPercentage
  - ZonedDateTime completedAt
  - LearningUnit learningUnit
  + markAsCompleted() void
  + setUserBlockProgresses(List~BlockProgress~) void
  + setCompleted(boolean) void
  + hashCode() int
  + isCompleted() boolean
  + setFirstOpenedAt(ZonedDateTime) void
  + getProgressPercentage() int
  + setOpened(boolean) void
  + getUuid() UUID
  + isOpened() boolean
  + getUserBlockProgresses() List~BlockProgress~
  # canEqual(Object) boolean
  + getFirstOpenedAt() ZonedDateTime
  + getLastOpenedAt() ZonedDateTime
  + setUser(User) void
  + setLearningUnit(LearningUnit) void
  + setCompletedAt(ZonedDateTime) void
  + removeBlockProgress(BlockProgress) void
  + setLearningKitProgress(LearningKitProgress) void
  + getUser() User
  + addBlockProgress(BlockProgress) void
  + getLearningUnit() LearningUnit
  + getLearningKitProgress() LearningKitProgress
  + toString() String
  + setLastOpenedAt(ZonedDateTime) void
  + setProgressPercentage(int) void
  + getCompletedAt() ZonedDateTime
  + markAsOpened() void
  + setUuid(UUID) void
  + equals(Object) boolean
}
class LearningUnitProgressRepository {
<<Interface>>
  + findAllByUser_UuidAndLearningKitProgress_LearningKit_Uuid(UUID, UUID) List~LearningUnitProgress~
  + findByUserAndLearningUnit(User, LearningUnit) Optional~LearningUnitProgress~
  + findByUser_UuidAndLearningUnit_Uuid(UUID, UUID) Optional~LearningUnitProgress~
  + findAllByLearningUnit_Uuid(UUID) List~LearningUnitProgress~
}
class LearningUnitProgressResDTO {
  - boolean isCompleted
  - int progressPercentage
  - String learningUnitId
  - List~BlockProgressResDTO~ userBlockProgresses
  - ZonedDateTime firstOpenedAt
  - ZonedDateTime lastOpenedAt
  - boolean isOpened
  - ZonedDateTime completedAt
  + isCompleted() boolean
  + progressPercentage() int
  + isOpened() boolean
  + learningUnitId() String
  + userBlockProgresses() List~BlockProgressResDTO~
  + completedAt() ZonedDateTime
  + firstOpenedAt() ZonedDateTime
  + lastOpenedAt() ZonedDateTime
}
class LearningUnitRepository {
<<Interface>>
  + updatePosition(int, UUID) void
  + findLearningUnitAsc(UUID) List~LearningUnit~
}
class LearningUnitResDTO {
  - List~BlockResDTO~ blocks
  - UUID uuid
  - int position
  - String name
  + name() String
  + position() int
  + uuid() UUID
  + blocks() List~BlockResDTO~
}
class LearningUnitRestController {
  - BlockService blockService
  - LearningUnitService learningUnitService
  - LearningUnitMapper learningUnitMapper
  - LearningKitRepository learningKitRepository
  - TemporaryKeyMapper temporaryKeyMapper
  + getLearningUnitById(UUID) LearningUnitResDTO
  + generateLearningUnit(UUID, GenerateLearningUnitDTO) LearningUnitResDTO
  + applyBlockActions(UUID, List~BlockActionDTO~) Map~String, UUID~
  + deleteLearningUnit(UUID) UUID
  + renameLearningUnit(RenameLearningUnitDTO, UUID) LearningUnitResDTO
  + createLearningUnit(CreateLearningUnitDTO) LearningUnitResDTO
}
class LearningUnitService {
  - LearningKitRepository learningKitRepository
  - LearningUnitRepository learningUnitRepository
  - TranslatedBlockRepository translatedBlockRepository
  - BlockRepository blockRepository
  - AIBlockService aiBlockService
  - LearningUnitProgressRepository learningUnitProgressRepository
  - getLearningUnit(UUID) LearningUnit
  + renameLearningUnit(UUID, String) LearningUnit
  + deleteById(UUID) void
  + createLearningUnit(LearningUnit, UUID) LearningUnit
  + generateLearningUnitWithAI(List~UUID~, UUID) LearningUnit
  + updateLearningUnitPosition(UUID, UpdateLearningUnitOrderDTO) void
  + findAll() List~LearningUnit~
  + findById(UUID) Optional~LearningUnit~
}
class LearningUnitTest {
  - LearningKit testLearningKit
  - LearningKitRepository learningKitRepository
  - Validator validator
  - LearningUnitRepository learningUnitRepository
  + setUpValidator() void
  + testLearningUnitCreationWithNullLearningKit() void
  + testLearningUnitCreationWithNameAndLearningKit() void
  + testLearningUnitAssociationWithLearningKit() void
  + testValidationOnUpdate() void
  - assertConstraintViolation(Set~ConstraintViolation~LearningUnit~~, String, Class~?~) void
  + setUp() void
  + testLearningUnitNameConstraints() void
  + testLearningUnitUuidGeneration() void
}
class LernelloApplication {
  - Logger log
  + main(String[]) void
}
class LernelloApplicationTests
class LoadDatabase {
  - PasswordEncoder encoder
  - Logger log
  - String theoryBlockContent
  ~ initDatabase(FolderRepository, UserRepository, LearningKitRepository, LearningUnitRepository, FileRepository, LearningKitService, LearningUnitService, BlockService) CommandLineRunner
}
class LoggedInUserDTO {
  - ZonedDateTime expires
  - String token
  + expires() ZonedDateTime
  + token() String
}
class MultipleChoiceAnswerValidationResDTO {
  - UUID blockId
  - boolean isCorrect
  + isCorrect() boolean
  + blockId() UUID
}
class MultipleChoiceBlock {
  - String question
  - List~String~ correctAnswers
  - List~String~ possibleAnswers
  + getCorrectAnswers() List~String~
  + getPossibleAnswers() List~String~
  + setPossibleAnswers(List~String~) void
  + hashCode() int
  + getQuestion() String
  + setQuestion(String) void
  + toString() String
  + setCorrectAnswers(List~String~) void
  + equals(Object) boolean
  # canEqual(Object) boolean
}
class MultipleChoiceBlockProgress {
  - List~String~ lastAnswers
  + getLastAnswers() List~String~
  + hashCode() int
  + setLastAnswers(List~String~) void
  + toString() String
  # canEqual(Object) boolean
  + equals(Object) boolean
}
class MultipleChoiceBlockProgressResDTO {
  - List~String~ lastAnswers
  - Boolean isCorrect
  - UUID blockId
  + isCorrect() Boolean
  + blockType() BlockType
  + blockId() UUID
  + lastAnswers() List~String~
}
class MultipleChoiceBlockResDTO {
  - UUID uuid
  - List~String~ correctAnswers
  - String question
  - String name
  - List~String~ possibleAnswers
  - BlockType type
  - int position
  - List~TranslatedBlockResDTO~ translatedContents
  + uuid() UUID
  + translatedContents() List~TranslatedBlockResDTO~
  + type() BlockType
  + name() String
  + question() String
  + correctAnswers() List~String~
  + possibleAnswers() List~String~
  + position() int
}
class ParentFolderMapper {
<<Interface>>
  + toDTO(Folder) ParentFolderResDTO
}
class ParentFolderMapperImpl {
  + toDTO(Folder) ParentFolderResDTO
}
class ParentFolderResDTO {
  - String name
  - UUID uuid
  + name() String
  + uuid() UUID
}
class ParticipantUserDTO {
  - UUID uuid
  - String name
  - String username
  - String surname
  + uuid() UUID
  + username() String
  + name() String
  + surname() String
}
class ParticipantUserMapper {
<<Interface>>
  + toDTO(User) ParticipantUserDTO
}
class ParticipantUserMapperImpl {
  + toDTO(User) ParticipantUserDTO
}
class ProbeResourceHttpRequestHandler {
  - detectMimeType(InputStream) String
  # getMediaType(HttpServletRequest, Resource) MediaType?
  - detectMimeType(byte[]) String
}
class ProgressMapper {
<<Interface>>
  + toQuestionBlockProgressResDTO(QuestionBlockProgress) QuestionBlockProgressResDTO
  + toLearningUnitProgressDTO(LearningUnitProgress) LearningUnitProgressResDTO
  + toBlockProgressResDTO(BlockProgress) BlockProgressResDTO
  + toTheoryBlockViewedResDTO(TheoryBlockProgress) TheoryBlockViewedResDTO
  + toLearningKitProgressResDTO(LearningKitProgress) LearningKitProgressResDTO
  + toMultipleChoiceBlockProgressResDTO(MultipleChoiceBlockProgress) MultipleChoiceBlockProgressResDTO
  + toTheoryBlockProgressResDTO(TheoryBlockProgress) TheoryBlockProgressResDTO
}
class ProgressMapperImpl {
  - learningKitProgressUserUuid(LearningKitProgress) UUID
  + toTheoryBlockViewedResDTO(TheoryBlockProgress) TheoryBlockViewedResDTO
  + toLearningKitProgressResDTO(LearningKitProgress) LearningKitProgressResDTO
  - multipleChoiceBlockProgressBlockUuid(MultipleChoiceBlockProgress) UUID
  + toLearningUnitProgressDTO(LearningUnitProgress) LearningUnitProgressResDTO
  - learningKitProgressLearningKitUuid(LearningKitProgress) UUID
  + toMultipleChoiceBlockProgressResDTO(MultipleChoiceBlockProgress) MultipleChoiceBlockProgressResDTO
  # blockProgressListToBlockProgressResDTOList(List~BlockProgress~) List~BlockProgressResDTO~
  + toQuestionBlockProgressResDTO(QuestionBlockProgress) QuestionBlockProgressResDTO
  - learningUnitProgressLearningUnitUuid(LearningUnitProgress) UUID
  - questionBlockProgressBlockUuid(QuestionBlockProgress) UUID
  - theoryBlockProgressBlockUuid(TheoryBlockProgress) UUID
  + toTheoryBlockProgressResDTO(TheoryBlockProgress) TheoryBlockProgressResDTO
}
class ProgressRestController {
  - ProgressService progressService
  - ProgressMapper progressMapper
  + markTheoryBlockViewed(TheoryBlockViewedDTO, UserDetails) TheoryBlockViewedResDTO
  + markLearningKitOpened(LearningKitOpened, UserDetails) LearningKitProgressResDTO
  + markLearningUnitOpened(LearningUnitOpenedDTO, UserDetails) LearningUnitProgressResDTO
  + checkQuestionAnswer(CheckQuestionAnswerDTO, UserDetails) QuestionAnswerValidationResDTO
  + checkMultipleChoiceAnswer(CheckMultipleChoiceAnswerDTO, UserDetails) MultipleChoiceAnswerValidationResDTO
  + getLearningUnitProgress(UUID, UserDetails) LearningUnitProgressResDTO
  + getLearningKitProgressForAllParticipants(UUID) List~LearningKitProgressResDTO~
  + getLearningKitProgress(UUID, UserDetails) LearningKitProgressResDTO
}
class ProgressService {
  - LearningKitProgressRepository learningKitProgressRepository
  - LearningUnitProgressRepository learningUnitProgressRepository
  - LearningUnitRepository learningUnitRepository
  - BlockRepository blockRepository
  - AIBlockService aiBlockService
  - LearningKitRepository learningKitRepository
  - UserService userService
  - BlockProgressRepository blockProgressRepository
  - updateLearningKitProgressPercentage(LearningKitProgress) void
  - getOrCreateLearningUnitProgress(User, LearningUnit, LearningKitProgress) LearningUnitProgress
  - updateLearningUnitProgressPercentage(LearningUnitProgress) void
  + checkMultipleChoiceAnswer(CheckMultipleChoiceAnswerDTO, UserDetails) MultipleChoiceAnswerValidationResDTO
  - getOrCreateBlockProgress(User, Block, LearningUnitProgress) BlockProgress
  - isBlockProgressConsideredComplete(BlockProgress) boolean
  + markLearningUnitOpened(LearningUnitOpenedDTO, UserDetails) LearningUnitProgress
  + markLearningKitOpened(LearningKitOpened, UserDetails) LearningKitProgress
  + checkQuestionAnswer(CheckQuestionAnswerDTO, UserDetails) QuestionAnswerValidationResDTO
  + getLearningKitProgressForAllParticipants(UUID) List~LearningKitProgress~
  + markTheoryBlockViewed(TheoryBlockViewedDTO, UserDetails) TheoryBlockProgress
  + getLearningKitProgress(UUID, UserDetails) LearningKitProgress
  - getOrCreateLearningKitProgress(User, LearningKit) LearningKitProgress
  + getLearningUnitProgress(UUID, UserDetails) LearningUnitProgress
}
class QuestionAnswerValidationResDTO {
  - boolean isCorrect
  - UUID blockId
  + blockId() UUID
  + isCorrect() boolean
}
class QuestionBlock {
  - String question
  - String expectedAnswer
  # canEqual(Object) boolean
  + equals(Object) boolean
  + getQuestion() String
  + setQuestion(String) void
  + hashCode() int
  + getExpectedAnswer() String
  + setExpectedAnswer(String) void
  + toString() String
}
class QuestionBlockProgress {
  - Integer scoreReached
  - String lastAnswer
  + toString() String
  + hashCode() int
  + getLastAnswer() String
  + setLastAnswer(String) void
  # canEqual(Object) boolean
  + getScoreReached() Integer
  + equals(Object) boolean
  + setScoreReached(Integer) void
}
class QuestionBlockProgressResDTO {
  - Boolean isCorrect
  - UUID blockId
  - String lastAnswer
  + isCorrect() Boolean
  + blockId() UUID
  + lastAnswer() String
  + blockType() BlockType
}
class QuestionBlockResDTO {
  - BlockType type
  - String name
  - int position
  - List~TranslatedBlockResDTO~ translatedContents
  - String expectedAnswer
  - String question
  - UUID uuid
  + position() int
  + name() String
  + expectedAnswer() String
  + translatedContents() List~TranslatedBlockResDTO~
  + uuid() UUID
  + question() String
  + type() BlockType
}
class RemoveBlockActionDTO {
  - BlockActionType type
  - String blockId
  + blockId() String
  + type() BlockActionType
}
class RenameLearningUnitDTO {
  - String name
  + name() String
}
class ReorderBlockActionDTO {
  - BlockActionType type
  - String blockId
  - int newIndex
  + newIndex() int
  + type() BlockActionType
  + blockId() String
}
class Role {
<<enumeration>>
  +  INSTRUCTOR
  +  TRAINEE
  + values() Role[]
  + valueOf(String) Role
}
class ScorableBlock {
  - Integer maxScore
  + equals(Object) boolean
  + hashCode() int
  + toString() String
  + getMaxScore() Integer
  + setMaxScore(Integer) void
  # canEqual(Object) boolean
}
class ScorableBlockProgress {
  - Integer scoreReached
  - Boolean isCorrect
  + getIsCorrect() Boolean
  + setScoreReached(Integer) void
  + equals(Object) boolean
  + getScoreReached() Integer
  + toString() String
  # canEqual(Object) boolean
  + setIsCorrect(Boolean) void
  + hashCode() int
}
class StaticResourceConfig {
  ~ probeResourceHttpRequestHandler() ResourceHttpRequestHandler
  ~ filesHandlerMapping(ResourceHttpRequestHandler) SimpleUrlHandlerMapping
}
class SubFolderResDTO {
  - UUID uuid
  - String name
  - List~SubFolderResDTO~ subFolders
  + uuid() UUID
  + subFolders() List~SubFolderResDTO~
  + name() String
}
class TemporaryKeyMapper {
<<Interface>>
  + toDTO(Map~String, UUID~) TemporaryKeySolveResDTO
}
class TemporaryKeyMapperImpl {
  + toDTO(Map~String, UUID~) TemporaryKeySolveResDTO
}
class TemporaryKeySolveResDTO {
  - Map~String, UUID~ temporaryKeyMap
  + temporaryKeyMap() Map~String, UUID~
}
class TheoryBlock {
  - String content
  + toString() String
  + hashCode() int
  + setContent(String) void
  # canEqual(Object) boolean
  + getContent() String
  + equals(Object) boolean
}
class TheoryBlockProgress {
  - Boolean isViewed
  + hashCode() int
  # canEqual(Object) boolean
  + getIsViewed() Boolean
  + toString() String
  + equals(Object) boolean
  + setIsViewed(Boolean) void
}
class TheoryBlockProgressResDTO {
  - UUID blockId
  - Boolean isViewed
  + blockType() BlockType
  + blockId() UUID
  + isViewed() Boolean
}
class TheoryBlockResDTO {
  - String content
  - List~TranslatedBlockResDTO~ translatedContents
  - int position
  - BlockType type
  - String name
  - UUID uuid
  + content() String
  + translatedContents() List~TranslatedBlockResDTO~
  + type() BlockType
  + name() String
  + position() int
  + uuid() UUID
}
class TheoryBlockViewedDTO {
  - UUID blockId
  + blockId() UUID
}
class TheoryBlockViewedResDTO {
  - Boolean isViewed
  - UUID blockId
  + isViewed() Boolean
  + blockId() UUID
}
class TranslatedBlock {
  - String content
  - BlockLanguage language
  - List~String~ correctAnswers
  - String question
  - String expectedAnswer
  - List~String~ possibleAnswers
  - Block originalBlock
  + getContent() String
  + setContent(String) void
  + setQuestion(String) void
  + setExpectedAnswer(String) void
  + setOriginalBlock(Block) void
  + setLanguage(BlockLanguage) void
  + getLanguage() BlockLanguage
  + getOriginalBlock() Block
  + getCorrectAnswers() List~String~
  + setPossibleAnswers(List~String~) void
  + getQuestion() String
  + getExpectedAnswer() String
  + setCorrectAnswers(List~String~) void
  + getPossibleAnswers() List~String~
}
class TranslatedBlockRepository {
<<Interface>>
  + findByOriginalBlock(Block) List~TranslatedBlock~
  + findByOriginalBlockIn(List~Block~) List~TranslatedBlock~
}
class TranslatedBlockResDTO {
  - String name
  - String expectedAnswer
  - UUID id
  - BlockLanguage language
  - String content
  - String question
  - List~String~ correctAnswers
  - List~String~ possibleAnswers
  + expectedAnswer() String
  + question() String
  + correctAnswers() List~String~
  + language() BlockLanguage
  + content() String
  + name() String
  + id() UUID
  + possibleAnswers() List~String~
}
class UpdateBlockActionDTO {
  - List~String~ correctAnswers
  - String question
  - String blockId
  - List~String~ possibleAnswers
  - BlockActionType type
  - UpdateBlockDTO data
  - String content
  - String expectedAnswer
  + blockId() String
  + content() String
  + possibleAnswers() List~String~
  + expectedAnswer() String
  + data() UpdateBlockDTO
  + question() String
  + correctAnswers() List~String~
  + type() BlockActionType
}
class UpdateBlockDTO {
<<Interface>>

}
class UpdateBlockNameActionDTO {
  - String blockId
  - BlockActionType type
  - String newName
  + blockId() String
  + type() BlockActionType
  + newName() String
}
class UpdateBlockOrderDTO {
  - List~UUID~ blockUuidsInOrder
  + blockUuidsInOrder() List~UUID~
}
class UpdateLearningKitDTO {
  - boolean published
  - List~UUID~ files
  - List~UUID~ participants
  - JsonNullable~String~ description
  - String name
  - JsonNullable~ZonedDateTime~ deadlineDate
  - JsonNullable~String~ context
  + description() JsonNullable~String~
  + files() List~UUID~
  + context() JsonNullable~String~
  + name() String
  + deadlineDate() JsonNullable~ZonedDateTime~
  + published() boolean
  + participants() List~UUID~
}
class UpdateLearningUnitOrderDTO {
  - List~UUID~ learningUnitUuidsInOrder
  + learningUnitUuidsInOrder() List~UUID~
}
class UpdateMultipleChoiceBlockDTO {
  - BlockType type
  - String name
  - UUID uuid
  - String question
  - List~String~ possibleAnswers
  - List~String~ correctAnswers
  + type() BlockType
  + possibleAnswers() List~String~
  + uuid() UUID
  + correctAnswers() List~String~
  + name() String
  + question() String
}
class UpdateQuestionBlockDTO {
  - BlockType type
  - String expectedAnswer
  - UUID uuid
  - String name
  - String question
  + question() String
  + name() String
  + type() BlockType
  + expectedAnswer() String
  + uuid() UUID
}
class UpdateTheoryBlockDTO {
  - String content
  - UUID uuid
  - String name
  - BlockType type
  + content() String
  + name() String
  + type() BlockType
  + uuid() UUID
}
class UpdateUserDTO {
  - Role role
  - String name
  - String username
  - String surname
  + name() String
  + username() String
  + surname() String
  + role() Role
}
class User {
  - String username
  - String surname
  - String name
  - String token
  - UUID uuid
  - Role role
  - LocalDateTime createDate
  - String locale
  - ZonedDateTime expires
  - String password
  - boolean changedPassword
  - LocalDateTime updateDate
  + getName() String
  + getExpires() ZonedDateTime
  + setUuid(UUID) void
  + getUpdateDate() LocalDateTime
  + getPassword() String
  + setLocale(String) void
  + setRole(Role) void
  + setCreateDate(LocalDateTime) void
  + getSurname() String
  + isChangedPassword() boolean
  + equals(Object) boolean
  + setSurname(String) void
  + setUpdateDate(LocalDateTime) void
  + getRole() Role
  + setUsername(String) void
  + toString() String
  + getUuid() UUID
  + setToken(String) void
  + getUsername() String
  + hashCode() int
  + getToken() String
  + getLocale() String
  + setExpires(ZonedDateTime) void
  + setChangedPassword(boolean) void
  # canEqual(Object) boolean
  + setName(String) void
  + setPassword(String) void
  + getCreateDate() LocalDateTime
}
class UserInfoDTO {
  - UUID uuid
  - Role role
  - String locale
  - String username
  - boolean changedPassword
  + uuid() UUID
  + changedPassword() boolean
  + locale() String
  + username() String
  + role() Role
}
class UserInfoMapper {
<<Interface>>
  + toDTO(User) UserInfoDTO
}
class UserInfoMapperImpl {
  + toDTO(User) UserInfoDTO
}
class UserLocaleDTO {
  - String locale
  + locale() String
}
class UserLocaleMapper {
<<Interface>>
  + toDTO(String) UserLocaleDTO
}
class UserLocaleMapperImpl {
  + toDTO(String) UserLocaleDTO
}
class UserLoginDTO {
  - String password
  - String username
  + username() String
  + password() String
}
class UserLoginMapper {
<<Interface>>
  + toDTO(User) LoggedInUserDTO
}
class UserLoginMapperImpl {
  + toDTO(User) LoggedInUserDTO
}
class UserMapper {
<<Interface>>
  + toDTO(User) UserResDTO
  + toEntity(UpdateUserDTO) User
}
class UserMapperImpl {
  + toEntity(UpdateUserDTO) User
  + toDTO(User) UserResDTO
}
class UserRepository {
<<Interface>>
  + findAllByRoleOrderBySurnameAscNameAsc(Role) List~User~
  + findByUuid(UUID) User
  + findByUsername(String) User
}
class UserResDTO {
  - String name
  - String username
  - Role role
  - UUID uuid
  - String surname
  + surname() String
  + username() String
  + role() Role
  + name() String
  + uuid() UUID
}
class UserRestController {
  - UserService userService
  - UserLocaleMapper userLocaleMapper
  - UserInfoMapper userInfoMapper
  - EmailService emailService
  - UserMapper userMapper
  - ParticipantUserMapper participantUserMapper
  + editUser(UUID, UpdateUserDTO) UserResDTO
  + createUser(CreateUserDTO) UserResDTO
  + resetUserPassword(UUID) UUID
  + setUserLocale(UserLocaleDTO, UserDetails) UserLocaleDTO
  + getAllInstructors() List~ParticipantUserDTO~
  + getUserInfo(UserDetails) UserInfoDTO
  + changePassword(ChangePasswordDataDTO, UserDetails) GenericSuccessDTO
  + getUser(UUID) UserResDTO
  + getAllTrainees() List~ParticipantUserDTO~
  + editTrainee(CreateParticipantDTO) ParticipantUserDTO
  + deleteUser(UUID) UUID
}
class UserService {
  - UserRepository userRepository
  - AuthenticationManager authenticationManager
  - PasswordEncoder passwordEncoder
  - LearningKitRepository learningKitRepository
  - JwtUtil jwtUtil
  + createUser(String, String, String, Role) User
  + deleteUser(UUID) void
  + findByUsername(String) User
  + findByUuid(UUID) User
  + findAllTrainees() List~User~
  - removeUserAllLearningKits(User) void
  + update(UUID, User) User
  + getUserFromUserDetails(UserDetails) User
  + generateRandomPassword() String
  + authenticate(String, String) User
  + editTrainee(String, String, String) User
  + changePassword(String, String) boolean
  + findAllByIds(List~UUID~) List~User~
  - removeInstructorFromLearningKits(User, User) void
  + setLocale(String, String) String
  + findAllInstructors() List~User~
}
class UserTest {
  - User user
  ~ setUp() void
  ~ testChangedPasswordDefault() void
  ~ testRoleCannotBeChanged() void
  ~ testConstructor() void
}
class WebConfig {
  + addResourceHandlers(ResourceHandlerRegistry) void
}
class WebSecurityConfig {
  + String[] WHITELIST_URLS
  - String corsAllowedOrigins
  ~ passwordEncoder() PasswordEncoder
  ~ authenticationJwtTokenFilter() AuthTokenFilter
  ~ authenticationManager(AuthenticationConfiguration) AuthenticationManager
  ~ corsConfigurationSource() CorsConfigurationSource
  ~ securityFilterChain(HttpSecurity) SecurityFilterChain
}
class settings {
  + main(String[]) void
  + run() Object
  + invokeMethod(String, Object) Object
  + setProperty(String, Object) void
  + setMetaClass(MetaClass) void
  + getProperty(String) Object
  + getMetaClass() MetaClass
}

AIBlockRestController "1" *--> "aiBlockService 1" AIBlockService 
AIBlockRestController "1" *--> "blockMapper 1" BlockMapper 
AIBlockService "1" *--> "aiClient 1" AIClient 
AIBlockService "1" *--> "blockRepository 1" BlockRepository 
AIBlockService "1" *--> "fileService 1" FileService 
AddBlockActionDTO  ..>  BlockActionDTO 
AuthRestController "1" *--> "userLoginMapper 1" UserLoginMapper 
AuthRestController "1" *--> "userService 1" UserService 
AuthTokenFilter "1" *--> "userDetailsService 1" CustomUserDetailsService 
AuthTokenFilter "1" *--> "jwtUtils 1" JwtUtil 
Block "1" *--> "type 1" BlockType 
Block "1" *--> "learningUnit 1" LearningUnit 
BlockMapperImpl  ..>  BlockMapper 
BlockMapperImpl  ..>  MultipleChoiceBlockResDTO : «create»
BlockMapperImpl  ..>  QuestionBlockResDTO : «create»
BlockMapperImpl  ..>  TheoryBlock : «create»
BlockMapperImpl  ..>  TheoryBlockResDTO : «create»
BlockProgress "1" *--> "block 1" Block 
BlockProgress "1" *--> "learningUnitProgress 1" LearningUnitProgress 
BlockProgress "1" *--> "user 1" User 
BlockService "1" *--> "blockProgressRepository 1" BlockProgressRepository 
BlockService "1" *--> "blockRepository 1" BlockRepository 
BlockService "1" *--> "learningUnitRepository 1" LearningUnitRepository 
CreateMultipleChoiceBlockDTO  ..>  CreateBlockDTO 
CreateQuestionBlockDTO  ..>  CreateBlockDTO 
CreateTheoryBlockDTO  ..>  CreateBlockDTO 
CustomUserDetailsService "1" *--> "userRepository 1" UserRepository 
EmailService "1" *--> "userRepository 1" UserRepository 
EmailService "1" *--> "userService 1" UserService 
EntityMapperImpl  ..>  EntityMapper 
EntityMapperImpl "1" *--> "jsonNullableMapper 1" JsonNullableMapper 
FileMapperImpl  ..>  FileMapper 
FileMapperImpl  ..>  FileResDTO : «create»
FileRestController "1" *--> "fileMapper 1" FileMapper 
FileRestController "1" *--> "fileService 1" FileSystemService 
FileSystemService "1" *--> "fileRepository 1" FileRepository 
FileSystemService  ..>  FileService 
FileSystemService "1" *--> "learningKitRepository 1" LearningKitRepository 
FileTest "1" *--> "testFile 1" File 
FileTest "1" *--> "fileRepository 1" FileRepository 
FolderMapperImpl  ..>  Folder : «create»
FolderMapperImpl  ..>  FolderMapper 
FolderMapperImpl  ..>  FolderResDTO : «create»
FolderMapperImpl "1" *--> "parentFolderMapper 1" ParentFolderMapper 
FolderMapperImpl  ..>  SubFolderResDTO : «create»
FolderRestController "1" *--> "folderMapper 1" FolderMapper 
FolderRestController "1" *--> "folderService 1" FolderService 
FolderService "1" *--> "folderRepository 1" FolderRepository 
FolderTest "1" *--> "rootFolder 1" Folder 
FolderTest "1" *--> "folderRepository 1" FolderRepository 
JacksonConfigTests "1" *--> "mapper 1" EntityMapper 
JsonNullableMapperImpl  ..>  JsonNullableMapper 
LearningKit "1" *--> "files *" File 
LearningKit "1" *--> "learningUnits *" LearningUnit 
LearningKit "1" *--> "participants *" User 
LearningKitMapperImpl  ..>  FileResDTO : «create»
LearningKitMapperImpl "1" *--> "fileService 1" FileService 
LearningKitMapperImpl "1" *--> "jsonNullableMapper 1" JsonNullableMapper 
LearningKitMapperImpl  ..>  LearningKit : «create»
LearningKitMapperImpl  ..>  LearningKitMapper 
LearningKitMapperImpl  ..>  LearningKitResDTO : «create»
LearningKitMapperImpl "1" *--> "learningUnitMapper 1" LearningUnitMapper 
LearningKitMapperImpl  ..>  ParticipantUserDTO : «create»
LearningKitMapperImpl "1" *--> "userService 1" UserService 
LearningKitProgress "1" *--> "learningKit 1" LearningKit 
LearningKitProgress "1" *--> "learningUnitProgresses *" LearningUnitProgress 
LearningKitProgress "1" *--> "user 1" User 
LearningKitRestController "1" *--> "customUserDetailsService 1" CustomUserDetailsService 
LearningKitRestController "1" *--> "learningKitMapper 1" LearningKitMapper 
LearningKitRestController "1" *--> "learningKitProgressRepository 1" LearningKitProgressRepository 
LearningKitRestController "1" *--> "learningKitService 1" LearningKitService 
LearningKitRestController "1" *--> "learningUnitService 1" LearningUnitService 
LearningKitRestController "1" *--> "userService 1" UserService 
LearningKitService "1" *--> "emailService 1" EmailService 
LearningKitService "1" *--> "learningKitRepository 1" LearningKitRepository 
LearningKitService "1" *--> "progressService 1" ProgressService 
LearningKitService "1" *--> "userRepository 1" UserRepository 
LearningKitTest "1" *--> "learningKitRepository 1" LearningKitRepository 
LearningUnit "1" *--> "blocks *" Block 
LearningUnit "1" *--> "learningKit 1" LearningKit 
LearningUnitMapper "1" *--> "translatedBlockRepository 1" TranslatedBlockRepository 
LearningUnitProgress "1" *--> "userBlockProgresses *" BlockProgress 
LearningUnitProgress "1" *--> "learningKitProgress 1" LearningKitProgress 
LearningUnitProgress "1" *--> "learningUnit 1" LearningUnit 
LearningUnitProgress "1" *--> "user 1" User 
LearningUnitRestController "1" *--> "blockService 1" BlockService 
LearningUnitRestController "1" *--> "learningKitRepository 1" LearningKitRepository 
LearningUnitRestController "1" *--> "learningUnitMapper 1" LearningUnitMapper 
LearningUnitRestController "1" *--> "learningUnitService 1" LearningUnitService 
LearningUnitRestController "1" *--> "temporaryKeyMapper 1" TemporaryKeyMapper 
LearningUnitService "1" *--> "aiBlockService 1" AIBlockService 
LearningUnitService "1" *--> "blockRepository 1" BlockRepository 
LearningUnitService "1" *--> "learningKitRepository 1" LearningKitRepository 
LearningUnitService "1" *--> "learningUnitProgressRepository 1" LearningUnitProgressRepository 
LearningUnitService "1" *--> "learningUnitRepository 1" LearningUnitRepository 
LearningUnitService "1" *--> "translatedBlockRepository 1" TranslatedBlockRepository 
LearningUnitTest "1" *--> "testLearningKit 1" LearningKit 
LearningUnitTest "1" *--> "learningKitRepository 1" LearningKitRepository 
LearningUnitTest "1" *--> "learningUnitRepository 1" LearningUnitRepository 
MultipleChoiceBlock  -->  ScorableBlock 
MultipleChoiceBlockProgress  -->  ScorableBlockProgress 
MultipleChoiceBlockProgressResDTO  ..>  BlockProgressResDTO 
MultipleChoiceBlockResDTO  ..>  BlockResDTO 
ParentFolderMapperImpl  ..>  ParentFolderMapper 
ParentFolderMapperImpl  ..>  ParentFolderResDTO : «create»
ParticipantUserMapperImpl  ..>  ParticipantUserDTO : «create»
ParticipantUserMapperImpl  ..>  ParticipantUserMapper 
ProgressMapperImpl  ..>  LearningKitProgressResDTO : «create»
ProgressMapperImpl  ..>  LearningUnitProgressResDTO : «create»
ProgressMapperImpl  ..>  MultipleChoiceBlockProgressResDTO : «create»
ProgressMapperImpl  ..>  ProgressMapper 
ProgressMapperImpl  ..>  QuestionBlockProgressResDTO : «create»
ProgressMapperImpl  ..>  TheoryBlockProgressResDTO : «create»
ProgressMapperImpl  ..>  TheoryBlockViewedResDTO : «create»
ProgressRestController "1" *--> "progressMapper 1" ProgressMapper 
ProgressRestController "1" *--> "progressService 1" ProgressService 
ProgressService "1" *--> "aiBlockService 1" AIBlockService 
ProgressService "1" *--> "blockProgressRepository 1" BlockProgressRepository 
ProgressService "1" *--> "blockRepository 1" BlockRepository 
ProgressService "1" *--> "learningKitProgressRepository 1" LearningKitProgressRepository 
ProgressService "1" *--> "learningKitRepository 1" LearningKitRepository 
ProgressService "1" *--> "learningUnitProgressRepository 1" LearningUnitProgressRepository 
ProgressService "1" *--> "learningUnitRepository 1" LearningUnitRepository 
ProgressService "1" *--> "userService 1" UserService 
QuestionBlock  -->  ScorableBlock 
QuestionBlockProgress  -->  ScorableBlockProgress 
QuestionBlockProgressResDTO  ..>  BlockProgressResDTO 
QuestionBlockResDTO  ..>  BlockResDTO 
RemoveBlockActionDTO  ..>  BlockActionDTO 
ReorderBlockActionDTO  ..>  BlockActionDTO 
ScorableBlock  -->  Block 
ScorableBlockProgress  -->  BlockProgress 
TemporaryKeyMapperImpl  ..>  TemporaryKeyMapper 
TemporaryKeyMapperImpl  ..>  TemporaryKeySolveResDTO : «create»
TheoryBlock  -->  Block 
TheoryBlockProgress  -->  BlockProgress 
TheoryBlockProgressResDTO  ..>  BlockProgressResDTO 
TheoryBlockResDTO  ..>  BlockResDTO 
TranslatedBlock "1" *--> "originalBlock 1" Block 
TranslatedBlock  -->  Block 
TranslatedBlock "1" *--> "language 1" BlockLanguage 
UpdateBlockActionDTO  ..>  BlockActionDTO 
UpdateBlockNameActionDTO  ..>  BlockActionDTO 
UpdateMultipleChoiceBlockDTO  ..>  UpdateBlockDTO 
UpdateQuestionBlockDTO  ..>  UpdateBlockDTO 
UpdateTheoryBlockDTO  ..>  UpdateBlockDTO 
User "1" *--> "role 1" Role 
UserInfoMapperImpl  ..>  UserInfoDTO : «create»
UserInfoMapperImpl  ..>  UserInfoMapper 
UserLocaleMapperImpl  ..>  UserLocaleDTO : «create»
UserLocaleMapperImpl  ..>  UserLocaleMapper 
UserLoginMapperImpl  ..>  LoggedInUserDTO : «create»
UserLoginMapperImpl  ..>  UserLoginMapper 
UserMapperImpl  ..>  User : «create»
UserMapperImpl  ..>  UserMapper 
UserMapperImpl  ..>  UserResDTO : «create»
UserRestController "1" *--> "emailService 1" EmailService 
UserRestController "1" *--> "participantUserMapper 1" ParticipantUserMapper 
UserRestController "1" *--> "userInfoMapper 1" UserInfoMapper 
UserRestController "1" *--> "userLocaleMapper 1" UserLocaleMapper 
UserRestController "1" *--> "userMapper 1" UserMapper 
UserRestController "1" *--> "userService 1" UserService 
UserService "1" *--> "jwtUtil 1" JwtUtil 
UserService "1" *--> "learningKitRepository 1" LearningKitRepository 
UserService "1" *--> "userRepository 1" UserRepository 
UserTest "1" *--> "user 1" User 
