import { AxiosApiGateway } from '../infra/config/axiosApiGateway';
import { JsonSerializer } from '../infra/config/jsonSerializer';
import AuthApiRepository from '../infra/repositories/authApiRepository';
import BackupIntervalApiRepository from '../infra/repositories/BackupIntervalApiRepository';
import BackupKeepIntervalApiRepository from '../infra/repositories/BackupKeepIntervalApiRepository';
import CompilerApiRepository from '../infra/repositories/CompilerApiRepository';
import ConfigurationApiRepository from '../infra/repositories/ConfigurationApiRepository';
import DataObjectApiRepository from '../infra/repositories/DataObjectApiRepository';
import DataObjectFileApiRepository from '../infra/repositories/DataObjectFileApiRepository';
import DataObjectLocationTypeApiRepository from '../infra/repositories/DataObjectLocationTypeApiRepository';
import DataObjectTypeApiRepository from '../infra/repositories/DataObjectTypeApiRepository';
import DirectoryApiRepository from '../infra/repositories/DirectoryApiRepository';
import FeatureApiRepository from '../infra/repositories/FeatureApiRepository';
import GeneratedCodeLanguageApiRepository from '../infra/repositories/GeneratedCodeLanguageApiRepository';
import LanguageApiRepository from '../infra/repositories/LanguageApiRepository';
import LocationApiRepository from '../infra/repositories/LocationApiRepository';
import LocationTypeApiRepository from '../infra/repositories/LocationTypeApiRepository';
import OrganizationApiRepository from '../infra/repositories/OrganizationApiRepository';
import PlatformTypeApiRepository from '../infra/repositories/PlatformTypeApiRepository';
import ProjectApiRepository from '../infra/repositories/ProjectApiRepository';
import RoleApiRepository from '../infra/repositories/RoleApiRepository';
import SettingApiRepository from '../infra/repositories/SettingApiRepository';
import SystemApiRepository from '../infra/repositories/SystemApiRepository';
import SystemTypeApiRepository from '../infra/repositories/SystemTypeApiRepository';
import TypeProjectElementApiRepository from '../infra/repositories/TypeProjectElementApiRepository';
import UserApiRepository from '../infra/repositories/UserApiRepository';
import UserSettingApiRepository from '../infra/repositories/UserSettingApiRepository';
import AuthService from '../services/authService';
import BackupIntervalService from '../services/BackupIntervalService';
import BackupKeepIntervalService from '../services/BackupKeepIntervalService';
import CompilerService from '../services/CompilerService';
import ConfigurationService from '../services/ConfigurationService';
import DataObjectFileService from '../services/DataObjectFileService';
import DataObjectLocationTypeService from '../services/DataObjectLocationTypeService';
import DataObjectService from '../services/DataObjectService';
import DataObjectTypeService from '../services/DataObjectTypeService';
import DirectoryService from '../services/DirectoryService';
import FeatureService from '../services/FeatureService';
import GeneratedCodeLanguageService from '../services/GeneratedCodeLanguageService';
import InteropService from '../services/interopService';
import LanguageService from '../services/LanguageService';
import LocationService from '../services/LocationService';
import LocationTypeService from '../services/LocationTypeService';
import OrganizationService from '../services/OrganizationService';
import PlatformTypeService from '../services/PlatformTypeService';
import ProjectService from '../services/ProjectService';
import RoleService from '../services/RoleService';
import SettingService from '../services/SettingService';
import SystemService from '../services/SystemService';
import SystemTypeService from '../services/SystemTypeService';
import TypeProjectElementService from '../services/TypeProjectElementService';
import UserService from '../services/UserService';
import UserSettingService from '../services/UserSettingService';

export const defaultInteropService = new InteropService();

const objectMapper = new JsonSerializer();
const defaultApiGateway = new AxiosApiGateway(objectMapper);

export const defaultAuthService = new AuthService(new AuthApiRepository(defaultApiGateway));
export const defaultOrganizationService = new OrganizationService(new OrganizationApiRepository(defaultApiGateway));
export const defaultUserService = new UserService(new UserApiRepository(defaultApiGateway));
export const defaultRoleService = new RoleService(new RoleApiRepository(defaultApiGateway));
export const defaultFeatureService = new FeatureService(new FeatureApiRepository(defaultApiGateway));
export const defaultSystemService = new SystemService(new SystemApiRepository(defaultApiGateway));
export const defaultProjectService = new ProjectService(new ProjectApiRepository(defaultApiGateway));
export const defaultBackupIntervalService = new BackupIntervalService(new BackupIntervalApiRepository(defaultApiGateway));
export const defaultBackupKeepIntervalService = new BackupKeepIntervalService(new BackupKeepIntervalApiRepository(defaultApiGateway));
export const defaultCompilerService = new CompilerService(new CompilerApiRepository(defaultApiGateway));
export const defaultGeneratedCodeLanguageService = new GeneratedCodeLanguageService(new GeneratedCodeLanguageApiRepository(defaultApiGateway));
export const defaultLanguageService = new LanguageService(new LanguageApiRepository(defaultApiGateway));
export const defaultSettingService = new SettingService(new SettingApiRepository(defaultApiGateway));
export const defaultConfigurationService = new ConfigurationService(new ConfigurationApiRepository(defaultApiGateway));
export const defaultDirectoryService = new DirectoryService(new DirectoryApiRepository(defaultApiGateway));
export const defaultUserSettingService = new UserSettingService(new UserSettingApiRepository(defaultApiGateway));
export const defaultLocationTypeService = new LocationTypeService(new LocationTypeApiRepository(defaultApiGateway));
export const defaultPlatformTypeService = new PlatformTypeService(new PlatformTypeApiRepository(defaultApiGateway));
export const defaultLocationService = new LocationService(new LocationApiRepository(defaultApiGateway));
export const defaultTypeProjectElementService = new TypeProjectElementService(new TypeProjectElementApiRepository(defaultApiGateway));
export const defaultSystemTypeService = new SystemTypeService(new SystemTypeApiRepository(defaultApiGateway));
export const defaultDataObjectService = new DataObjectService(new DataObjectApiRepository(defaultApiGateway));
export const defaultDataObjectTypeService = new DataObjectTypeService(new DataObjectTypeApiRepository(defaultApiGateway));
export const defaultDataObjectLocationTypeService = new DataObjectLocationTypeService(new DataObjectLocationTypeApiRepository(defaultApiGateway));
export const defaultDataObjectFileService = new DataObjectFileService(new DataObjectFileApiRepository(defaultApiGateway));
