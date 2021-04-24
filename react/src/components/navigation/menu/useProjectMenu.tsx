import { useTranslation } from 'react-i18next';
import { EntityType } from '../../../features/common/EntityType';
import Feature from '../../../lib/domain/entities/Feature';
import Project from '../../../lib/domain/entities/Project';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useProjectMenu = (toggleDialog: (modalType: EntityType) => void) => {

    const {i18n} = useTranslation(['navigation']);
    const { openForm, deleteItem } = useMenuItemActions();

    const getProjectMenuTitle = ():string => {
        return i18n.t('navigation:project:projects');
    };

    const getProjectMenuItems = (defaultProject: Project | undefined): MenuItem[] => {
        const projectIdentifier = defaultProject ? ' - ' + defaultProject.identifier : '';
        const idProject = defaultProject ? defaultProject.id : undefined;
        return [
            {
                title: i18n.t('navigation:set') + projectIdentifier,
                features: [Feature.PROJECT_MANAGEMENT, Feature.PROJECT_DATA_MAPS, Feature.PROJECT_TABLES],
                onClick: () => toggleDialog('Project')
            },
            {
                title: i18n.t('navigation:search'),
                features: [Feature.PROJECT_DATA_MAPS, Feature.PROJECT_TABLES]
            },
            {
                title: i18n.t('navigation:manage'),
                items: [
                    {
                        title: i18n.t('navigation:create'),
                        items: [
                            {
                                title: i18n.t('navigation:new'),
                                features: [Feature.PROJECT_MANAGEMENT],
                                onClick: () => openForm('Project')
                            },
                            {
                                title: i18n.t('navigation:copy'),
                                features: [Feature.PROJECT_MANAGEMENT]
                            },
                            {
                                title: i18n.t('navigation:import'),
                                features: [Feature.PROJECT_MANAGEMENT]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:edit'),
                        disable: !defaultProject,
                        features: [Feature.PROJECT_MANAGEMENT],
                        onClick: () => openForm('Project', idProject)
                    },
                    {
                        title: i18n.t('navigation:export'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:download'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:details'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        disable: !defaultProject,
                        features: [Feature.PROJECT_MANAGEMENT],
                        onClick: () => deleteItem('Project', defaultProject)
                    }
                ]
            },
            {
                title: i18n.t('navigation:project:maps'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:search'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:manage'),
                        items: [
                            {
                                title: i18n.t('navigation:create'),
                                items: [
                                    {
                                        title: i18n.t('navigation:new'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:project:createTemplate'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:import'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:copy'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:edit'),
                                items: [
                                    {
                                        title: i18n.t('navigation:edit'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:project:textForm'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:project:appendTargets'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:view'),
                                items: [
                                    {
                                        title: i18n.t('navigation:project:activeMap'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:project:textForm'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    },
                                    {
                                        title: i18n.t('navigation:messages'),
                                        features: [Feature.PROJECT_DATA_MAPS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:delete'),
                                features: [Feature.PROJECT_DATA_MAPS]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:export'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:validate'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:synchronize'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    }
                ]
            },
            {
                title: i18n.t('navigation:project:tables'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.PROJECT_TABLES]
                    },
                    {
                        title: i18n.t('navigation:manage'),
                        items: [
                            {
                                title: i18n.t('navigation:create'),
                                items: [
                                    {
                                        title: i18n.t('navigation:new'),
                                        features: [Feature.PROJECT_TABLES]
                                    },
                                    {
                                        title: i18n.t('navigation:import'),
                                        features: [Feature.PROJECT_TABLES]
                                    },
                                    {
                                        title: i18n.t('navigation:copy'),
                                        features: [Feature.PROJECT_TABLES]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:edit'),
                                items: [
                                    {
                                        title: i18n.t('navigation:project:definition'),
                                        features: [Feature.PROJECT_TABLES]
                                    },
                                    {
                                        title: i18n.t('navigation:project:entries'),
                                        features: [Feature.PROJECT_TABLES]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:view'),
                                features: [Feature.PROJECT_TABLES]
                            },
                            {
                                title: i18n.t('navigation:delete'),
                                features: [Feature.PROJECT_TABLES]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:synchronize'),
                        features: [Feature.PROJECT_TABLES]
                    }
                ]
            },
            {
                title: i18n.t('navigation:tools'),
                items: [
                    {
                        title: i18n.t('navigation:project:buildCsvInputProgram'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:project:buildCsvOutputProgram'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:project:buildJsonOutputProgram'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:project:buildOIPAOutputProgram'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    }
                ]
            },
            {
                title: i18n.t('navigation:synchronize'),
                items: [
                    {
                        title: i18n.t('navigation:project:transferProject'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    },
                    {
                        title: i18n.t('navigation:project:transferError'),
                        features: [Feature.PROJECT_DATA_MAPS]
                    }
                ]
            },
            {
                title: i18n.t('navigation:project:archives'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:create'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:project:freezeLabel'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:project:recover'),
                        features: [Feature.PROJECT_MANAGEMENT, Feature.PROJECT_DATA_MAPS, Feature.PROJECT_TABLES]
                    },
                    {
                        title: i18n.t('navigation:settings'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        features: [Feature.PROJECT_MANAGEMENT]
                    }
                ]
            }
        ];
    };

    return { getProjectMenuTitle, getProjectMenuItems };
};
