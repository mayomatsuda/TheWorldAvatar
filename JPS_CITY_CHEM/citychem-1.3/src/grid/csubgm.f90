! <csubgm.f90 - A component of the City-scale
!                 Chemistry Transport Model EPISODE-CityChem>
!*****************************************************************************! 
!* 
!* EPISODE - An urban-scale air quality model
!* ========================================== 
!* Copyright (C) 2018  NILU - Norwegian Institute for Air Research
!*                     Instituttveien 18
!*                     PO Box 100
!*                     NO-2027 Kjeller
!*                     Norway
!*
!*                     Contact persons: Gabriela Sousa Santos - gss@nilu.no
!*                                      Paul Hamer - pdh@nilu.no
!*
!* Unless explicitly acquired and licensed from Licensor under another license,
!* the contents of this file are subject to the Reciprocal Public License ("RPL")
!* Version 1.5, https://opensource.org/licenses/RPL-1.5 or subsequent versions as
!* allowed by the RPL, and You may not copy or use this file in either source code
!* or executable form, except in compliance with the terms and conditions of the RPL. 
!*
!* All software distributed under the RPL is provided strictly on an "AS IS" basis, 
!* WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY 
!* DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF
!* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT.
!* See the RPL for specific language governing rights and limitations under the RPL.
!*
!* ========================================== 
!* The dispersion model EPISODE (Grønskei et. al., 1993; Larssen et al., 1994;
!* Walker et al., 1992, 1999; Slørdal et al., 2003, 2008) is an Eulerian grid model 
!* with embedded subgrid models for calculations of pollutant concentrations resulting 
!* from different types of sources (area-, line- and point sources). EPISODE solves the 
!* time dependent advection/-diffusion equation on a 3 dimensional grid. 
!* Finite difference numerical methods are applied to integrate the solution forward in time. 
!* It also includes extensions as the implementation of a simplified EMEP photochemistry 
!* scheme for urban areas (Walker et al. 2004) and a scheme for Secondary Organic Aerosol 
!* implemented by Håvard Slørdal
!*
!* Grønskei, K.E., Walker, S.E., Gram, F. (1993) Evaluation of a model for hourly spatial
!*    concentrations distributions. Atmos. Environ., 27B, 105-120.
!* Larssen, S., Grønskei, K.E., Gram, F., Hagen, L.O., Walker, S.E. (1994) Verification of 
!*    urban scale time-dependent dispersion model with sub-grid elements in Oslo, Norway. 
!*    In: Air poll. modelling and its appl. X. New York, Plenum Press.
!* Slørdal, L.H., McInnes, H., Krognes, T. (2008): The Air Quality Information System AirQUIS. 
!*    Info. Techn. Environ. Eng., 1, 40-47, 2008.
!* Slørdal, L.H., Walker, S.-E., Solberg, S. (2003) The Urban Air Dispersion Model EPISODE 
!*    applied in AirQUIS. Technical Description. NILU TR 12/2003. ISBN 82-425-1522-0.
!* Walker, S.E., Grønskei, K.E. (1992) Spredningsberegninger for on-line overvåking i Grenland. 
!*    Programbeskrivelse og brukerveiledning. Lillestrøm, 
!*    Norwegian Institute for Air Research (NILU OR 55/92).
!* Walker, S.E., Slørdal, L.H., Guerreiro, C., Gram, F., Grønskei, K.E. (1999) Air pollution 
!*    exposure monitoring and estimation. Part II. Model evaluation and population exposure. 
!*    J. Environ. Monit, 1, 321-326.
!* Walker, S.-E., Solberg, S., Denby, B. (2003) Development and implementation of a 
!*    simplified EMEP photochemistry scheme for urban areas in EPISODE. NILU TR 13/2013. 
!*    ISBN 82-425-1524-7
!*****************************************************************************! 

      subroutine CSUBGM(ICV,IDV,IWV)

!     The subroutine calculates grid model concentrations and dry and
!     wet depositions in all main grid cells.
! ----------------------------------------------------------------------------------
! Based on:
! Version Episode 5.5 (May29, 2012) prepared for BB-Stand-Alone
! Original source code of EPISODE by Sam-Erik Walker (NILU)
!
! Sam-Erik Walker
! Norwegian Institute for Air Research (NILU)
! Instituttveien 18 P.O. Box 100
! N-2027 Kjeller, NORWAY
! Tel: +47 63898000 Fax: +47 63898050
! E-mail: sam-erik.walker@nilu.no
!
! ----------------------------------------------------------------------------------
! ----------------------------------------------------------------------------------
! REVISION HISTORY
!
!           2016  M. Karl: Avoid double-counting of sub-grid concentrations. 
!                          Should be CM = CRV, not CM = CM + CRV
!
! ----------------------------------------------------------------------------------

      use mod_util
      use mod_main
      use mod_site
      use mod_time
      use mod_mete
      use mod_conc
      use mod_depo
      use mod_grid

      implicit none

      integer :: ICV
      integer :: IDV
      integer :: IWV

!     ICV - Update concentration  indicator
!     IDV - Update dry deposition indicator
!     IWV - Update wet deposition indicator

!     Local variables

!      real,allocatable :: CRV(:)
      double precision,allocatable :: CRV(:)
      real,allocatable :: DDRV(:)
      real,allocatable :: WDRV(:)
      real :: XRV
      real :: YRV
      real :: ZRV

      integer :: IC
      integer :: IX
      integer :: IY

!     CRV  - Receptor concentration  values
!     DDRV - Receptor dry deposition values
!     WDRV - Receptor wet deposition values
!     XRV  - Receptor x-coordinate
!     YRV  - Receptor y-coordinate
!     ZRV  - Receptor z-coordinate
!     IC   - Compound index
!     IX   - Main grid index in x-direction
!     IY   - Main grid index in y-direction

!_DYN_ALLOC_Start:
      IF (.NOT. ALLOCATED(CRV))  ALLOCATE(CRV(MC))
      IF (.NOT. ALLOCATED(DDRV)) ALLOCATE(DDRV(MC))
      IF (.NOT. ALLOCATED(WDRV)) ALLOCATE(WDRV(MC))
!_DYN_ALLOC_End.

!     Go through all main grid cells

      !print *,'csubgm CM before CR', CM(1,16,6)

      DO 100 IY = 1,NY
        DO 110 IX = 1,NX

          XRV = SITEX0 + (IX - 0.5)*DX
          YRV = SITEY0 + (IY - 0.5)*DY
!_LHS_Aug_2007_Start:
!          ZRV = 1.0                        ! "Ground Level".
          ZRV = 0.5*DZ(1)                   ! Midpoint Layer 1.
!          ZRV = DZ(1)+(0.5*DZ(2))          ! Midpoint Layer 2.
!          ZRV = DZ(1)+DZ(2)+(0.5*DZ(3))    ! Midpoint Layer 3.
!          ZRV = DZ(1)+DZ(2)+DZ(3)+(0.5*DZ(4))
!          ZRV = DZ(1)+DZ(2)+DZ(3)+DZ(4)+(0.5*DZ(5))
!          ZRV = DZ(1)+DZ(2)+DZ(3)+DZ(4)+DZ(5)+(0.5*DZ(6))
!          ZRV = DZ(1)+DZ(2)+DZ(3)+DZ(4)+DZ(5)+DZ(6)
!     &          +(0.5*DZ(7))
!          ZRV = DZ(1)+DZ(2)+DZ(3)+DZ(4)+DZ(5)+DZ(6)
!     &          +DZ(7)+(0.5*DZ(8))
          ZRV = (ZRV * DEPTHM(IX,IY))/MOD_H 
!_LHS_Aug_2007_End.


! Calculate concentrations and dry and wet depositions

          call csubg(XRV,YRV,ZRV,NC,MC,CRV,DDRV,WDRV)

! Go through all compounds

          DO 120 IC = 1,NC

!_LHS_Hourly_Averaged_Output_December_2007_Start:

!MSK start
! This looks like double-counting since in csubg: CRV = C
! Should be CM = CRV, not CM = CM + CRV

            IF (averaged_output) then

! ***         Add concentration  to current main grid cell:

!MSK              IF (ICV .EQ. 1) CM(IX,IY,IC) = CM(IX,IY,IC) +     &
!MSK                                           (CRV(IC)/FLOAT(NTS))
               IF (ICV .EQ. 1)   CM(IX,IY,IC) = CM(IX,IY,IC) +     &
                                             (CRV(IC)/FLOAT(NTS))

            ELSE

! ***         Add concentration  to current main grid cell:


!MSK              IF (ICV .EQ. 1) CM(IX,IY,IC) = CM(IX,IY,IC) +  CRV(IC)
               IF (ICV .EQ. 1)    CM(IX,IY,IC) = CRV(IC)
!MSK end
            ENDIF
!_LHS_Hourly_Averaged_Output_December_2007_End.

!MSK Currently dry and wet deposition in csubg are set to zero

! ***       Add dry deposition to current main grid cell

            IF (IDV .EQ. 1) DDEPM(IX,IY,IC) = DDEPM(IX,IY,IC) + DDRV(IC)

! ***       Add wet deposition to current main grid cell

            IF (IWV .EQ. 1) WDEPM(IX,IY,IC) = WDEPM(IX,IY,IC) + WDRV(IC)

! ***     Next compound

  120     CONTINUE

!MSK debug start
!MSK debug                if ( (iy==20).and.(ix==20) ) then
!MSK debug                   print *,'csubgm O3', ICV,IDV,IWV, CM(ix,iy,1) , CRV(1)
!MSK debug                   print *,'csubgm CO', ICV,IDV,IWV, CM(ix,iy,6) , CRV(6)
!MSK debug                   print *,'csubgm NO2',ICV,IDV,IWV, CM(ix,iy,3) , CRV(3)
!MSK debug                   print *,'csubgm NO', ICV,IDV,IWV, CM(ix,iy,2) , CRV(2)
!MSK debug                endif
!MSK debug end

! ***   Next main grid cell

  110   CONTINUE
  100 CONTINUE

      !print *,'csubgm CM after CR', CM(1,16,6)

!_DYN_ALLOC_Start:
      IF (ALLOCATED(CRV))  DEALLOCATE(CRV)
      IF (ALLOCATED(DDRV)) DEALLOCATE(DDRV)
      IF (ALLOCATED(WDRV)) DEALLOCATE(WDRV)
!_DYN_ALLOC_End.

      RETURN

! End of subroutine CSUBGM

      end subroutine csubgm