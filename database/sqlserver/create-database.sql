USE [master]
GO

CREATE DATABASE [DcaDb]
 CONTAINMENT = NONE
 ON  PRIMARY
( NAME = N'DcaDb', FILENAME = N'/var/opt/mssql/data/DcaDb.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON
( NAME = N'DcaDb_log', FILENAME = N'/var/opt/mssql/data/DcaDb_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO

IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [DcaDb].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO

ALTER DATABASE [DcaDb] SET ANSI_NULL_DEFAULT OFF
GO

ALTER DATABASE [DcaDb] SET ANSI_NULLS OFF
GO

ALTER DATABASE [DcaDb] SET ANSI_PADDING OFF
GO

ALTER DATABASE [DcaDb] SET ANSI_WARNINGS OFF
GO

ALTER DATABASE [DcaDb] SET ARITHABORT OFF
GO

ALTER DATABASE [DcaDb] SET AUTO_CLOSE OFF
GO

ALTER DATABASE [DcaDb] SET AUTO_SHRINK OFF
GO

ALTER DATABASE [DcaDb] SET AUTO_UPDATE_STATISTICS ON
GO

ALTER DATABASE [DcaDb] SET CURSOR_CLOSE_ON_COMMIT OFF
GO

ALTER DATABASE [DcaDb] SET CURSOR_DEFAULT  GLOBAL
GO

ALTER DATABASE [DcaDb] SET CONCAT_NULL_YIELDS_NULL OFF
GO

ALTER DATABASE [DcaDb] SET NUMERIC_ROUNDABORT OFF
GO

ALTER DATABASE [DcaDb] SET QUOTED_IDENTIFIER OFF
GO

ALTER DATABASE [DcaDb] SET RECURSIVE_TRIGGERS OFF
GO

ALTER DATABASE [DcaDb] SET  DISABLE_BROKER
GO

ALTER DATABASE [DcaDb] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO

ALTER DATABASE [DcaDb] SET DATE_CORRELATION_OPTIMIZATION OFF
GO

ALTER DATABASE [DcaDb] SET TRUSTWORTHY OFF
GO

ALTER DATABASE [DcaDb] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO

ALTER DATABASE [DcaDb] SET PARAMETERIZATION SIMPLE
GO

ALTER DATABASE [DcaDb] SET READ_COMMITTED_SNAPSHOT OFF
GO

ALTER DATABASE [DcaDb] SET HONOR_BROKER_PRIORITY OFF
GO

ALTER DATABASE [DcaDb] SET RECOVERY FULL
GO

ALTER DATABASE [DcaDb] SET  MULTI_USER
GO

ALTER DATABASE [DcaDb] SET PAGE_VERIFY CHECKSUM
GO

ALTER DATABASE [DcaDb] SET DB_CHAINING OFF
GO

ALTER DATABASE [DcaDb] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF )
GO

ALTER DATABASE [DcaDb] SET TARGET_RECOVERY_TIME = 60 SECONDS
GO

ALTER DATABASE [DcaDb] SET DELAYED_DURABILITY = DISABLED
GO

ALTER DATABASE [DcaDb] SET QUERY_STORE = OFF
GO

ALTER DATABASE [DcaDb] SET READ_WRITE
GO
