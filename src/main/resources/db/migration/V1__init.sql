CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
  id UUID PRIMARY KEY,
  email TEXT UNIQUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE preferences (
  user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
  timezone TEXT NOT NULL DEFAULT 'America/New_York',
  translation TEXT NOT NULL DEFAULT 'ESV',
  topics JSONB NOT NULL DEFAULT '[]'::jsonb,
  reminder_start_time TIME NOT NULL DEFAULT '08:00',
  reminder_end_time TIME NOT NULL DEFAULT '21:00',
  reminders_per_day INT NOT NULL DEFAULT 3,
  delivery_channels JSONB NOT NULL DEFAULT '["EMAIL"]'::jsonb
);

CREATE TABLE daily_lessons (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  lesson_date DATE NOT NULL,
  theme TEXT NOT NULL,
  primary_reference TEXT NOT NULL,
  summary TEXT NOT NULL,
  bullets JSONB NOT NULL DEFAULT '[]'::jsonb,
  reflection_questions JSONB NOT NULL DEFAULT '[]'::jsonb,
  prayer TEXT,
  action_step TEXT,
  supporting_references JSONB NOT NULL DEFAULT '[]'::jsonb,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE (user_id, lesson_date)
);

CREATE TYPE reminder_channel AS ENUM ('PUSH','EMAIL','SMS');
CREATE TYPE reminder_status AS ENUM ('PENDING','SENT','FAILED');

CREATE TABLE reminders (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  lesson_id UUID NOT NULL REFERENCES daily_lessons(id) ON DELETE CASCADE,
  scheduled_at TIMESTAMPTZ NOT NULL,
  channel reminder_channel NOT NULL,
  title TEXT NOT NULL,
  message TEXT NOT NULL,
  supporting_reference TEXT,
  status reminder_status NOT NULL DEFAULT 'PENDING',
  sent_at TIMESTAMPTZ,
  error_message TEXT
);

CREATE INDEX idx_reminders_due
  ON reminders (status, scheduled_at);

CREATE TABLE device_tokens (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  platform TEXT NOT NULL,
  token TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  last_seen_at TIMESTAMPTZ,
  UNIQUE (user_id, token)
);