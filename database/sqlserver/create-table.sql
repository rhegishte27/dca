USE [DcaDb]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Organizations](
	[PK_Organizations] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Description] [nvarchar](255) NOT NULL,
 CONSTRAINT [PK_Organizations] PRIMARY KEY CLUSTERED
(
	[PK_Organizations] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Organizations] ADD CONSTRAINT [UQ_Organizations_Name] UNIQUE([Name])
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Users](
	[PK_Users] [int] IDENTITY(1,1) NOT NULL,
	[FK_Organizations] [int] NULL,
	[Role] [int] NOT NULL,
	[Identifier] [nvarchar](8) NOT NULL,
	[UserName] [nvarchar](32) NOT NULL,
	[PwdHash] [char](72) NOT NULL,
	[EmailAddress] [nvarchar](32) NOT NULL,
	[Language] [int] NULL
 CONSTRAINT [PK_Users] PRIMARY KEY CLUSTERED
(
	[PK_Users] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Users] ADD CONSTRAINT [UQ_Users_Identifier] UNIQUE([Identifier])
GO
ALTER TABLE [dbo].[Users]  WITH CHECK ADD CONSTRAINT [FK_Users_Organizations] FOREIGN KEY([FK_Organizations])
REFERENCES [dbo].[Organizations] ([PK_Organizations])
GO
ALTER TABLE [dbo].[Users] CHECK CONSTRAINT [FK_Users_Organizations]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Systems](
	[PK_Systems] [int] IDENTITY(1,1) NOT NULL,
	[Identifier] [nvarchar](8) NOT NULL,
	[Description] [nvarchar](255) NOT NULL,
	[DataDictionary] [varbinary](max),
	[DatetimeDDUpdated] [datetime] NULL
 CONSTRAINT [PK_Systems] PRIMARY KEY CLUSTERED
(
	[PK_Systems] DESC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Systems] ADD CONSTRAINT [UQ_Systems_Identifier] UNIQUE([Identifier])
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Projects](
	[PK_Projects] [int] IDENTITY(1,1) NOT NULL,
	[Identifier] [nvarchar](8) NOT NULL,
	[Description] [nvarchar](255) NOT NULL,
	[GeneratedCodeLanguage] [int] NOT NULL,
	[Compiler] [int] NOT NULL,
	[SyncEnabled] [bit] NOT NULL,
	[BackupsEnabled] [bit] NOT NULL,
	[BackupInterval] [int] NULL,
	[NumBackupIntervals] [int] NULL,
	[BackupKeepInterval] [int] NULL,
	[NumBackupKeepIntervals] [int] NULL,
	[SynchronizeProjectElementsEnabled] [bit] NOT NULL,
	[LastBackupTime] [datetime] NULL,
 CONSTRAINT [PK_Projects] PRIMARY KEY CLUSTERED
(
	[PK_Projects] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Projects] ADD CONSTRAINT [UQ_Projects_Identifier] UNIQUE([Identifier])
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[UsersFeatures](
	[FK_Users] [int] NOT NULL,
	[Feature] [int] NOT NULL
 CONSTRAINT [PK_UsersFeatures] PRIMARY KEY CLUSTERED
(
	[FK_Users], [Feature]
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[UsersFeatures]  WITH CHECK ADD  CONSTRAINT [FK_UsersFeatures_Users] FOREIGN KEY([FK_Users])
REFERENCES [dbo].[Users] ([PK_Users])
GO
ALTER TABLE [dbo].[UsersFeatures] CHECK CONSTRAINT [FK_UsersFeatures_Users]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[DCASettings](
    [PK_Settings] [int] IDENTITY(1,1) NOT NULL,
	[Language] [int] NOT NULL,
	[TokenDuration] [int] NOT NULL,
	[CommonFolder] [nvarchar](256) NOT NULL,
	[DefaultImportFolder] [nvarchar](256) NOT NULL,
	[DefaultExportFolder] [nvarchar](256) NOT NULL,
	[DefaultDownloadFolder] [nvarchar](256) NOT NULL
CONSTRAINT [PK_Settings] PRIMARY KEY CLUSTERED
(
	[PK_Settings] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Locations](
    [PK_Locations] [int] IDENTITY(1,1) NOT NULL,
	[Identifier] [nvarchar](50) NOT NULL,
	[Type] [int] NOT NULL,
	[Path] [nvarchar](100) NOT NULL,
	[ServerName] [nvarchar](100) NULL,
	[UserName] [nvarchar](50) NULL,
	[Password] [nvarchar](50) NULL,
	[PlatformType] [int] NULL
CONSTRAINT [PK_Locations] PRIMARY KEY CLUSTERED
(
	[PK_Locations] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Locations] ADD CONSTRAINT [UQ_Locations_Identifier] UNIQUE([Identifier])
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[ProjectSystems](
    [FK_Projects] [int] NOT NULL,
    [FK_Systems] [int] NOT NULL,
    [SystemType] [int] NOT NULL,
	[SyncEnabled] [bit] NOT NULL,
    [FK_Locations] [int] NULL
CONSTRAINT [PK_ProjectSystems] PRIMARY KEY CLUSTERED
(
	[FK_Projects], [FK_Systems] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProjectSystems] WITH CHECK ADD CONSTRAINT [FK_ProjectSystems_Projects] FOREIGN KEY([FK_Projects])
REFERENCES [dbo].[Projects] ([PK_Projects])
GO
ALTER TABLE [dbo].[ProjectSystems] CHECK CONSTRAINT [FK_ProjectSystems_Projects]
GO
ALTER TABLE [dbo].[ProjectSystems] WITH CHECK ADD CONSTRAINT [FK_ProjectSystems_Systems] FOREIGN KEY([FK_Systems])
REFERENCES [dbo].[Systems] ([PK_Systems])
GO
ALTER TABLE [dbo].[ProjectSystems] CHECK CONSTRAINT [FK_ProjectSystems_Systems]
GO
ALTER TABLE [dbo].[ProjectSystems] WITH CHECK ADD CONSTRAINT [FK_ProjectSystems_Locations] FOREIGN KEY([FK_Locations])
REFERENCES [dbo].[Locations] ([PK_Locations])
GO
ALTER TABLE [dbo].[ProjectSystems] CHECK CONSTRAINT [FK_ProjectSystems_Locations]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[DataObjects](
    [PK_DataObjects] [int] IDENTITY(1,1) NOT NULL,
	[Identifier] [nvarchar](8) NOT NULL,
	[Description] [nvarchar](40) NOT NULL,
	[Status] [int] NOT NULL,
    [FK_Systems] [int] NOT NULL,
    [Type] [int] NOT NULL
CONSTRAINT [PK_DataObjects] PRIMARY KEY CLUSTERED
(
	[PK_DataObjects] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[DataObjects] ADD CONSTRAINT [UQ_DataObjects_Identifier_Systems] UNIQUE([Identifier], [FK_Systems])
GO
ALTER TABLE [dbo].[DataObjects] WITH CHECK ADD CONSTRAINT [FK_DataObjects_Systems] FOREIGN KEY([FK_Systems])
REFERENCES [dbo].[Systems] ([PK_Systems])
GO
ALTER TABLE [dbo].[DataObjects] CHECK CONSTRAINT [FK_DataObjects_Systems]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[DataObjectFiles](
    [PK_DataObjectFiles] [int] IDENTITY(1,1) NOT NULL,
    [FK_DataObjects] [int] NOT NULL,
    [Status] [int] NOT NULL,
    [OriginalFilename] [nvarchar](150) NOT NULL,
    [DataObjectContent] [nvarchar](MAX) NOT NULL,
    [ResultContent] [nvarchar](MAX) NOT NULL,
	[DateTimeUploaded] [datetime] NOT NULL,
    [FK_Users] [int] NOT NULL
CONSTRAINT [PK_DataObjectFiles] PRIMARY KEY CLUSTERED
(
	[PK_DataObjectFiles] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[DataObjectFiles] WITH CHECK ADD CONSTRAINT [FK_DataObjectFiles_DataObjects] FOREIGN KEY([FK_DataObjects])
REFERENCES [dbo].[DataObjects] ([PK_DataObjects])
GO
ALTER TABLE [dbo].[DataObjectFiles] CHECK CONSTRAINT [FK_DataObjectFiles_DataObjects]
GO
ALTER TABLE [dbo].[DataObjectFiles] WITH CHECK ADD CONSTRAINT [FK_DataObjectFiles_Users] FOREIGN KEY([FK_Users])
REFERENCES [dbo].[Users] ([PK_Users])
GO
ALTER TABLE [dbo].[DataObjectFiles] CHECK CONSTRAINT [FK_DataObjectFiles_Users]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[ProjectSyncSettings] (
    [FK_Projects] [int] NOT NULL,
    [TypeElement] [int] NOT NULL,
	[SyncEnabled] [bit] NOT NULL,
	[FK_Locations] [int] NULL
CONSTRAINT [PK_ProjectSyncSettings] PRIMARY KEY CLUSTERED
(
	[FK_Projects], [TypeElement] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[ProjectSyncSettings] WITH CHECK ADD CONSTRAINT [FK_ProjectSyncSettings_Projects] FOREIGN KEY([FK_Projects])
REFERENCES [dbo].[Projects] ([PK_Projects])
GO
ALTER TABLE [dbo].[ProjectSyncSettings] CHECK CONSTRAINT [FK_ProjectSyncSettings_Projects]
GO
ALTER TABLE [dbo].[ProjectSyncSettings] WITH CHECK ADD CONSTRAINT [FK_ProjectSyncSettings_Locations] FOREIGN KEY([FK_Locations])
REFERENCES [dbo].[Locations] ([PK_Locations])
GO
ALTER TABLE [dbo].[ProjectSyncSettings] CHECK CONSTRAINT [FK_ProjectSyncSettings_Locations]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[DataObjectFields](
    [PK_DataObjectFields] [bigint] IDENTITY(1,1) NOT NULL,
    [FK_DataObjects] [int] NOT NULL,
    [OrderNumber] [int] NOT NULL,
    [Name] [nvarchar](32) NOT NULL,
    [DataType] [int] NOT NULL,
    [DDlType] [nvarchar](60) NOT NULL,
    [Level] [int] NOT NULL,
    [OriginalName] [nvarchar](32) NULL,
    [Redefines] [nvarchar](32) NULL,
    [Occurs] [nvarchar](256) NULL,
    [Size] [int] NULL,
    [RawDeclaration] [nvarchar](256) NULL,
    [Value] [nvarchar](MAX) NULL,
    [Message] [nvarchar](512) NULL,
    [Status] [int] NULL
CONSTRAINT [PK_DataObjectFields] PRIMARY KEY CLUSTERED
(
	[PK_DataObjectFields] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[DataObjectFields] WITH CHECK ADD CONSTRAINT [FK_DataObjectFields_DataObjects] FOREIGN KEY([FK_DataObjects])
REFERENCES [dbo].[DataObjects] ([PK_DataObjects])
GO
ALTER TABLE [dbo].[DataObjectFields] CHECK CONSTRAINT [FK_DataObjectFields_DataObjects]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[UserSettings](
    [FK_Users] [int] NOT NULL,
	[FK_DefaultSystem] [int] NULL,
	[FK_DefaultProject] [int] NULL,
	[FK_DefaultOrganization] [int] NULL,
	[Fk_DefaultUser] [int] NULL,
	[Fk_DefaultLocation] [int] NULL,
	[Fk_DefaultDataObject] [int] NULL
CONSTRAINT [PK_UserSettings] PRIMARY KEY CLUSTERED
(
	[FK_Users] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, FILLFACTOR = 90) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_Users] FOREIGN KEY([FK_Users])
REFERENCES [dbo].[Users] ([PK_Users])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_Users]
GO
ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_DefaultSystem] FOREIGN KEY([FK_DefaultSystem])
REFERENCES [dbo].[Systems] ([PK_Systems])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_DefaultSystem]
GO
ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_DefaultProject] FOREIGN KEY([FK_DefaultProject])
REFERENCES [dbo].[Projects] ([PK_Projects])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_DefaultProject]
GO
ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_DefaultOrganization] FOREIGN KEY([FK_DefaultOrganization])
REFERENCES [dbo].[Organizations] ([PK_Organizations])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_DefaultOrganization]
GO
ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_DefaultUser] FOREIGN KEY([FK_DefaultUser])
REFERENCES [dbo].[Users] ([PK_Users])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_DefaultUser]
GO
ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_DefaultLocation] FOREIGN KEY([FK_DefaultLocation])
REFERENCES [dbo].[Locations] ([PK_Locations])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_DefaultLocation]
GO
ALTER TABLE [dbo].[UserSettings] WITH CHECK ADD CONSTRAINT [FK_UserSettings_DefaultDataObject] FOREIGN KEY([Fk_DefaultDataObject])
REFERENCES [dbo].[DataObjects] ([PK_DataObjects])
GO
ALTER TABLE [dbo].[UserSettings] CHECK CONSTRAINT [FK_UserSettings_DefaultDataObject]
GO
