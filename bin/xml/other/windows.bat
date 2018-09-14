@echo off
REM
REM  Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
REM 
REM  This program and the accompanying materials are made available under the
REM  terms of the Eclipse Public License v. 2.0, which is available at
REM  http://www.eclipse.org/legal/epl-2.0.
REM 
REM  This Source Code may also be made available under the following Secondary
REM  Licenses when the conditions for such availability set forth in the
REM  Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
REM  version 2 with the GNU Classpath Exception, which is available at
REM  https://www.gnu.org/software/classpath/license.html.
REM 
REM  SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
REM

@setlocal

if ""%1""=="""" goto end
if ""%1""==""startcmd"" goto startcmd

goto end

:startcmd
set newdir="%2"
start /d%newdir%
goto end

:end
@endlocal
