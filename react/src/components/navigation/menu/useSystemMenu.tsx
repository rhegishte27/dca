import { useTranslation } from 'react-i18next';
import { EntityType } from '../../../features/common/EntityType';
import DataObject from '../../../lib/domain/entities/DataObject';
import DataObjectContainer from '../../../lib/domain/entities/DataObjectContainer';
import Feature from '../../../lib/domain/entities/Feature';
import System from '../../../lib/domain/entities/System';
import { MenuItem } from '../MenuItem';
import useMenuItemActions from '../useMenuItemActions';

export const useSystemMenu = (toggleDialog: (modalType: EntityType) => void) => {

    const {i18n} = useTranslation(['navigation']);
    const { openForm, deleteItem } = useMenuItemActions();

    const getSystemMenuTitle = ():string => {
        return i18n.t('navigation:system:systems');
    };

    const getSystemMenuItems = (defaultSystem: System | undefined, defaultDataObject: DataObject | undefined): MenuItem[] => {
        const systemIdentifier = defaultSystem ? ' - ' + defaultSystem.identifier : '';
        const dataObjectIdentifier = defaultDataObject ? ' - ' + defaultDataObject.systemIdentifier + '-' + defaultDataObject.identifier : '';

        const idSystem = defaultSystem ? defaultSystem.id : undefined;
        const idDataObject = defaultDataObject ? defaultDataObject.id : undefined;
        return [
            {
                title: i18n.t('navigation:set') + systemIdentifier,
                features: [Feature.SYSTEM_MANAGEMENT, Feature.SYSTEM_CODE_ANALYSIS, Feature.SYSTEM_DATA_OBJECTS, Feature.SYSTEM_TRANSACTIONS],
                onClick: () => toggleDialog('System')
            },
            {
                title: i18n.t('navigation:search'),
                features: [Feature.SYSTEM_CODE_ANALYSIS, Feature.SYSTEM_DATA_OBJECTS, Feature.SYSTEM_TRANSACTIONS]
            },
            {
                title: i18n.t('navigation:manage'),
                items: [
                    {
                        title: i18n.t('navigation:create'),
                        items: [
                            {
                                title: i18n.t('navigation:new'),
                                features: [Feature.SYSTEM_MANAGEMENT],
                                onClick: () => openForm('System')
                            },
                            {
                                title: i18n.t('navigation:copy'),
                                features: [Feature.SYSTEM_MANAGEMENT]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:edit'),
                        features: [Feature.SYSTEM_MANAGEMENT],
                        disable: !defaultSystem,
                        onClick: () => openForm('System', idSystem)
                    },
                    {
                        title: i18n.t('navigation:view'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:details'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        disable: !defaultSystem,
                        features: [Feature.SYSTEM_MANAGEMENT],
                        onClick: () => deleteItem('System', defaultSystem)
                    }
                ]
            },
            {
                title: i18n.t('navigation:system:dataObjects'),
                items: [
                    {
                        title: i18n.t('navigation:set') + dataObjectIdentifier,
                        features: [Feature.SYSTEM_DATA_OBJECTS],
                        onClick: () => toggleDialog('DataObject')
                    },
                    {
                        title: i18n.t('navigation:search'),
                        features: [Feature.SYSTEM_DATA_OBJECTS]
                    },
                    {
                        title: i18n.t('navigation:manage'),
                        items: [
                            {
                                title: i18n.t('navigation:create'),
                                items: [
                                    {
                                        title: i18n.t('navigation:new'),
                                        features: [Feature.SYSTEM_DATA_OBJECTS]
                                    },
                                    {
                                        title: i18n.t('navigation:system:createFromFile'),
                                        features: [Feature.SYSTEM_DATA_OBJECTS],
                                        disable: !defaultSystem,
                                        onClick: () => openForm('DataObjectContainer', new DataObjectContainer(defaultSystem))
                                    },
                                    {
                                        title: i18n.t('navigation:copy'),
                                        features: [Feature.SYSTEM_DATA_OBJECTS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:edit'),
                                features: [Feature.SYSTEM_DATA_OBJECTS]
                            },
                            {
                                title: i18n.t('navigation:system:editSource'),
                                features: [Feature.SYSTEM_DATA_OBJECTS],
                                disable: !defaultDataObject,
                                onClick: () => openForm('DataObjectSource', idDataObject)
                            },
                            {
                                title: i18n.t('navigation:view'),
                                features: [Feature.SYSTEM_DATA_OBJECTS]
                            },
                            {
                                title: i18n.t('navigation:delete'),
                                features: [Feature.SYSTEM_DATA_OBJECTS]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:synchronize'),
                        features: [Feature.SYSTEM_DATA_OBJECTS]
                    }
                ]
            },
            {
                title: i18n.t('navigation:system:transactions'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.SYSTEM_TRANSACTIONS]
                    },
                    {
                        title: i18n.t('navigation:manage'),
                        items: [
                            {
                                title: i18n.t('navigation:create'),
                                items: [
                                    {
                                        title: i18n.t('navigation:new'),
                                        features: [Feature.SYSTEM_TRANSACTIONS]
                                    },
                                    {
                                        title: i18n.t('navigation:import'),
                                        features: [Feature.SYSTEM_TRANSACTIONS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:edit'),
                                items: [
                                    {
                                        title: i18n.t('navigation:system:transaction'),
                                        features: [Feature.SYSTEM_TRANSACTIONS]
                                    },
                                    {
                                        title: i18n.t('navigation:system:delimiters'),
                                        features: [Feature.SYSTEM_TRANSACTIONS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:view'),
                                features: [Feature.SYSTEM_TRANSACTIONS]
                            },
                            {
                                title: i18n.t('navigation:delete'),
                                features: [Feature.SYSTEM_TRANSACTIONS]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:system:children'),
                        items: [
                            {
                                title: i18n.t('navigation:set'),
                                features: [Feature.SYSTEM_TRANSACTIONS]
                            },
                            {
                                title: i18n.t('navigation:create'),
                                features: [Feature.SYSTEM_TRANSACTIONS]
                            },
                            {
                                title: i18n.t('navigation:edit'),
                                features: [Feature.SYSTEM_TRANSACTIONS]
                            },
                            {
                                title: i18n.t('navigation:delete'),
                                features: [Feature.SYSTEM_TRANSACTIONS]
                            }
                        ]
                    }
                ]
            },
            {
                title: i18n.t('navigation:system:xmlSchemas'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.SYSTEM_DATA_OBJECTS]
                    },
                    {
                        title: i18n.t('navigation:search'),
                        features: [Feature.SYSTEM_DATA_OBJECTS]
                    },
                    {
                        title: i18n.t('navigation:manage'),
                        items: [
                            {
                                title: i18n.t('navigation:create'),
                                items: [
                                    {
                                        title: i18n.t('navigation:new'),
                                        features: [Feature.SYSTEM_DATA_OBJECTS]
                                    },
                                    {
                                        title: i18n.t('navigation:import'),
                                        features: [Feature.SYSTEM_DATA_OBJECTS]
                                    },
                                    {
                                        title: i18n.t('navigation:copy'),
                                        features: [Feature.SYSTEM_DATA_OBJECTS]
                                    }
                                ]
                            },
                            {
                                title: i18n.t('navigation:edit'),
                                features: [Feature.SYSTEM_DATA_OBJECTS]
                            },
                            {
                                title: i18n.t('navigation:view'),
                                features: [Feature.SYSTEM_DATA_OBJECTS]
                            },
                            {
                                title: i18n.t('navigation:delete'),
                                features: [Feature.SYSTEM_DATA_OBJECTS]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:synchronize'),
                        features: [Feature.SYSTEM_DATA_OBJECTS]
                    }
                ]
            },
            {
                title: i18n.t('navigation:system:codeAnalysis'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.SYSTEM_CODE_ANALYSIS]
                    },
                    {
                        title: i18n.t('navigation:import'),
                        items: [
                            {
                                title: i18n.t('navigation:system:codeFiles'),
                                features: [Feature.SYSTEM_CODE_ANALYSIS]
                            },
                            {
                                title: i18n.t('navigation:system:results'),
                                features: [Feature.SYSTEM_CODE_ANALYSIS]
                            }
                        ]
                    },
                    {
                        title: i18n.t('navigation:system:processFiles'),
                        features: [Feature.SYSTEM_CODE_ANALYSIS]
                    },
                    {
                        title: i18n.t('navigation:system:showAnalysis'),
                        features: [Feature.SYSTEM_CODE_ANALYSIS]
                    },
                    {
                        title: i18n.t('navigation:view'),
                        features: [Feature.SYSTEM_CODE_ANALYSIS]
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        features: [Feature.SYSTEM_CODE_ANALYSIS]
                    }
                ]
            },
            {
                title: i18n.t('navigation:system:configuration'),
                items: [
                    {
                        title: i18n.t('navigation:set'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:import'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:view'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:delete'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    }
                ]
            },
            {
                title: i18n.t('navigation:tools'),
                items: [
                    {
                        title: i18n.t('navigation:compare'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    },
                    {
                        title: i18n.t('navigation:adjustNames'),
                        features: [Feature.SYSTEM_MANAGEMENT]
                    }
                ]
            },
            {
                title: i18n.t('navigation:synchronize'),
                features: [Feature.SYSTEM_MANAGEMENT]
            }
        ];
    };

    return { getSystemMenuTitle, getSystemMenuItems };
};
