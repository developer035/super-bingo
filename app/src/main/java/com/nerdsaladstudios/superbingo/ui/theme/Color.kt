package com.nerdsaladstudios.superbingo.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * SUPER BINGO DESIGN SYSTEM - "Classic Board Game" Palette
 *
 * Rules:
 * - Utility screens use [BaseOffWhite]
 * - Primary CTAs and Hero numbers use [RubyRed] WITH a 4-5px border of [OutlineBlack]
 * - Secondary accents and Hero backgrounds use [PeacockTeal]
 */

// ==========================================
// BACKGROUNDS
// ==========================================
val BaseOffWhite = Color(0xFFF8F4EF) // Clean, high-contrast base for all utility screens

// ==========================================
// BRAND & ACCENTS
// ==========================================
val PeacockTeal = Color(0xFF006D77)  // Primary accent (Hero backgrounds, secondary buttons)
val RubyRed     = Color(0xFF9D0208)  // Primary CTA buttons, destructive actions, hero numbers
val SoftGold    = Color(0xFFC7A97A)  // Highlight accents (trophies, winning tickets, coins)

// ==========================================
// FUNCTIONAL & UI
// ==========================================
val SuccessGreen = Color(0xFF4CAF50) // For previously called numbers on the board
val TextNavy     = Color(0xFF1B2A4A) // Deep indigo-navy for primary text (softer than pure black)
val TextMuted    = Color(0xFF7A8294) // Muted grey-navy for subtext and placeholder text
val OutlineBlack = Color(0xFF1A1A1A) // Thick 4-5px outline used ONLY on CTA buttons & hero numbers
val SurfaceWhite = Color(0xFFFFFFFF) // For cards and modal dialog backgrounds

// ==========================================
// ILLUSTRATION SPECIFIC (Characters Only)
// ==========================================
val SkinToneTan  = Color(0xFFE8A766) // Standard skin tone for onboarding/empty state characters



// For numbers that have already been called.
// Replaces the generic green with our rich brand teal for a premium feel.
val NumberCalled = Color(0xFF006D77)   // Peacock Teal

// For the hero number currently being called.
// Replaces the generic red with our deeper brand red.
// NOTE: Remember to apply the OutlineBlack border to this specific state!
val NumberCurrent = Color(0xFF9D0208)  // Ruby Red

// For numbers sitting on the board waiting to be called.
// Replaces the cool/purplish grey with a warm, subtle beige that blends
// beautifully with the BaseOffWhite (#F8F4EF) background.
val NumberUncalled = Color(0xFFEBE6DF) // Warm Muted Beige