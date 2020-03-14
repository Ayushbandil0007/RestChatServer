
USE [ChatServer]
GO

/****** Object:  Table [dbo].[MESSAGE_TABLE]    Script Date: 12/2/2020 10:40:43 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[MESSAGE_TABLE](
	[TOPIC] [nchar](20) NULL,
	[MESSAGES] [nvarchar](max) NULL,
	[TIME] [int] NULL,
	[USERNAME] [nchar](20) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO


USE [ChatServer]
GO

/****** Object:  Table [dbo].[USER_TABLE]    Script Date: 12/2/2020 10:41:01 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[USER_TABLE](
	[USERNAME] [nchar](20) NULL,
	[PASSWORD] [nchar](20) NULL,
	[ACTIVE] [bit] NULL
) ON [PRIMARY]
GO
